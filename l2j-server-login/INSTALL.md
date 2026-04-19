# 🛡️ L2J Login Server — полная инструкция

Полное руководство по установке, настройке, запуску и администрированию
логин-сервера L2J с учётом всех правок безопасности, мониторинга и 2FA,
добавленных в этом форке.

---

## 📑 Оглавление

- [🎯 Что умеет этот форк](#-что-умеет-этот-форк)
- [📋 Требования](#-требования)
- [☕ Шаг 1. Установка Java 21](#-шаг-1-установка-java-21)
- [🐬 Шаг 2. Установка MySQL 8](#-шаг-2-установка-mysql-8)
- [🗄️ Шаг 3. База данных](#️-шаг-3-база-данных)
- [⚙️ Шаг 4. Конфигурация](#️-шаг-4-конфигурация)
- [🔨 Шаг 5. Сборка из исходников](#-шаг-5-сборка-из-исходников)
- [🚀 Шаг 6. Запуск](#-шаг-6-запуск)
- [🐳 Запуск в Docker](#-запуск-в-docker)
- [🔒 Безопасность](#-безопасность)
  - [🔑 Пароли (PBKDF2 + миграция)](#-пароли-pbkdf2--миграция)
  - [🔐 TOTP 2FA](#-totp-2fa)
  - [🚫 CIDR-бан подсетей](#-cidr-бан-подсетей)
  - [🔐 TLS между LS и GS](#-tls-между-ls-и-gs)
  - [🛑 Блокировки и брутфорс](#-блокировки-и-брутфорс)
- [📊 Мониторинг](#-мониторинг)
  - [🩺 Health-check HTTP](#-health-check-http)
  - [📈 JMX-метрики](#-jmx-метрики)
  - [📜 Audit-лог](#-audit-лог)
  - [📝 JSON-логи](#-json-логи)
- [💻 Telnet-консоль](#-telnet-консоль)
- [🧩 Подключение Game Server](#-подключение-game-server)
- [🧯 Troubleshooting](#-troubleshooting)
- [❓ FAQ](#-faq)

---

## 🎯 Что умеет этот форк

Сверх стандартного L2J Login Server:

| Фича | Описание |
|------|----------|
| 🔑 PBKDF2-SHA256 пароли | 600k итераций, соль, lazy-миграция со старого SHA-1 при логине |
| 🚨 Per-account lockout | Экспоненциальный backoff (1 мин → 1 час) против брутфорса одного логина |
| 🔐 TOTP 2FA | RFC 6238, совместимо с Google Authenticator/Authy, без внешних зависимостей |
| 🚫 CIDR-бан | Бан подсетей `a.b.c.0/24`, IPv4 и IPv6 |
| 🌐 IPv6 | Полная поддержка: accept-фильтр, ban-check, SQL-колонки |
| 🔐 TLS LS↔GS | Опциональный SSLServerSocket для канала Login ↔ Game Server |
| 🩺 Health-check | HTTP-эндпоинты `/health` и `/metrics` на отдельном порту |
| 📈 JMX | Runtime-метрики в jconsole/VisualVM |
| 📜 Audit log | Таблица `login_audit` с событиями LOGIN_OK/FAIL, PASSWORD_CHANGE, BAN и т.д. |
| 📝 JSON logs | NDJSON-файл для ELK/Loki без внешних зависимостей |
| 💻 Telnet-админка | Команды `totp-set`, `pw-stats`, `ban-subnet`, `pw-expire-legacy` |
| 🐳 Docker | Готовый `Dockerfile` + `docker-compose.yml` с MySQL |
| 🛑 Graceful shutdown | Закрытие селектора, БД-пула, audit-очереди при SIGTERM |
| 🔒 Trust boundary | Защита от rogue Game Server: ChangeAccessLevel/ChangePassword/TempBan/SendMail проверяют владельца аккаунта |

---

## 📋 Требования

- 🖥 **ОС**: Windows 10+, Linux (Debian/Ubuntu/CentOS), macOS.
- ☕ **Java**: **JDK 21** (Eclipse Temurin, OpenJDK, Corretto, Zulu — любой).
- 🐬 **MySQL**: 8.0+ (или совместимый MariaDB 10.5+).
- 💾 **Диск**: ~100 МБ для сервера и БД, +место под логи.
- 🌐 **Порты**:
  - `2106/tcp` — клиенты L2.
  - `9014/tcp` — Game Server'ы к этому LS.
  - `8080/tcp` — health-check (опционально).
  - Telnet-порт из конфига (по умолчанию 12345) — **только на localhost**.

---

## ☕ Шаг 1. Установка Java 21

### 🪟 Windows

**Вариант через winget** (быстро):

```powershell
winget install EclipseAdoptium.Temurin.21.JDK
```

**Вариант через MSI-инсталлятор**:

1. Скачайте с https://adoptium.net/temurin/releases/?version=21&package=jdk&os=windows
2. Выберите **Windows x64 JDK MSI**.
3. При установке поставьте галочки:
   - ✅ `Set JAVA_HOME variable`
   - ✅ `JavaSoft (Oracle) registry keys`
   - ✅ `Add to PATH`
4. **Перезапустите** все открытые терминалы.

**Проверка**:

```cmd
java -version
javac -version
```

Должно показать `21.x.x`.

### 🐧 Linux (Debian/Ubuntu)

```bash
sudo apt update
sudo apt install -y wget apt-transport-https
wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | \
    sudo gpg --dearmor -o /usr/share/keyrings/adoptium.gpg
echo "deb [signed-by=/usr/share/keyrings/adoptium.gpg] \
    https://packages.adoptium.net/artifactory/deb $(lsb_release -cs) main" | \
    sudo tee /etc/apt/sources.list.d/adoptium.list
sudo apt update
sudo apt install -y temurin-21-jdk
```

Проверка:

```bash
java -version
```

### 🐧 Linux (CentOS/RHEL/Rocky)

```bash
sudo yum install -y wget
wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.rpm
sudo yum localinstall -y jdk-21_linux-x64_bin.rpm
```

### 🍏 macOS

```bash
brew install --cask temurin@21
```

---

## 🐬 Шаг 2. Установка MySQL 8

### 🪟 Windows

Скачайте MySQL Installer: https://dev.mysql.com/downloads/installer/

Установите **MySQL Server 8.0**. Задайте пароль пользователя `root` и запомните.

### 🐧 Debian/Ubuntu

```bash
sudo apt install -y mysql-server
sudo mysql_secure_installation
```

### 🐧 CentOS/RHEL

```bash
sudo yum install -y https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm
sudo yum install -y mysql-server
sudo systemctl enable --now mysqld
```

---

## 🗄️ Шаг 3. База данных

### 3.1. Создайте схему и пользователя

```bash
mysql -u root -p
```

```sql
CREATE DATABASE l2jdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'l2j'@'localhost' IDENTIFIED BY 'СМЕНИТЕ_МЕНЯ';
GRANT ALL PRIVILEGES ON l2jdb.* TO 'l2j'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3.2. Накатите схему (новая установка)

```bash
cd /path/to/server
mysql -u l2j -p l2jdb < login/sql/accounts.sql
mysql -u l2j -p l2jdb < login/sql/accounts_ipauth.sql
mysql -u l2j -p l2jdb < login/sql/account_data.sql
mysql -u l2j -p l2jdb < login/sql/gameservers.sql
mysql -u l2j -p l2jdb < login/sql/login_audit.sql
```

### 3.3. Миграция с предыдущей версии

> ⚠️ Только если у вас **уже работает** старый login-server и вы апгрейдите.

```bash
mysql -u l2j -p l2jdb < login/sql/migrations/2026_04_19_password_pbkdf2_and_ipv6.sql
mysql -u l2j -p l2jdb < login/sql/migrations/2026_04_19_totp_audit.sql
```

Что сделают миграции:

- 📏 `accounts.password` → `VARCHAR(256)` (под PBKDF2).
- 🌐 IP-колонки → `VARCHAR(45)` (поддержка IPv6).
- 🔑 `accounts.totp_secret VARCHAR(64)` — новое поле для 2FA.
- 🔍 Индекс `accounts_ipauth.login` (было full-scan).
- 📜 Таблица `login_audit`.

Старые пароли SHA-1 **продолжат работать** и автоматически мигрируются в PBKDF2
при первом успешном логине каждого игрока.

---

## ⚙️ Шаг 4. Конфигурация

Все конфиги — в `login/config/`. Редактируйте под себя.

### 📄 `server.properties`

```properties
# --- Сеть ---
Host = *                       # bind IP для клиентов (* = все интерфейсы)
Port = 2106                    # клиентский порт
GameServerHost = 127.0.0.1     # bind IP для GS (если LS и GS на одной машине — localhost)
GameServerPort = 9014

# --- Security ---
LoginTryBeforeBan = 10         # сколько неверных попыток с IP до бана IP
LoginBlockAfterBan = 600       # длительность IP-бана в секундах
AcceptNewGameServer = False    # True только если нужна регистрация новых GS

# --- Auto-create ---
AutoCreateAccounts = True      # создавать аккаунт при первом вводе логина
AutoCreateAccountsAccessLevel = 0

# --- Health-check (новое) ---
HealthCheckEnabled = True
HealthCheckHost = 127.0.0.1
HealthCheckPort = 8080

# --- TLS LS<->GS (новое, опционально) ---
GameServerTlsEnabled = False
GameServerTlsKeystore = config/tls/keystore.p12
GameServerTlsKeystorePassword = changeit
```

### 📄 `database.properties`

```properties
URL = jdbc:mysql://localhost:3306/l2jdb?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
User = l2j
Password = СМЕНИТЕ_МЕНЯ
MaximumPoolSize = 10
```

### 📄 `telnet.properties`

```properties
Enabled = True
Port = 12345
Password = ОБЯЗАТЕЛЬНО_СМЕНИТЕ
# Разрешённые хосты (строго лучше localhost):
Hosts = 127.0.0.1
```

> ⚠️ Если `Password` не задан, сервер сгенерирует случайный при старте, но **НЕ запишет его в plain-лог** (из соображений безопасности) — задайте сами.

### 📄 `banned_ip.cfg`

Формат:

```
# Комментарии с #
# Точный IP:
192.168.0.55
# IP с датой истечения (миллисекунды от epoch):
10.0.0.5 1893456000000
# Подсеть CIDR (новое в этом форке):
10.0.0.0/24
2001:db8::/32
```

---

## 🔨 Шаг 5. Сборка из исходников

```bash
cd l2j-server-login

# Windows (bash) / Linux / macOS:
./mvnw -DskipTests package

# Windows (cmd):
mvnw.cmd -DskipTests package
```

Результат: `target/l2jlogin.jar` (~150 КБ) + `target/libs/` (зависимости).
Также собирается zip-дистрибутив: `target/l2j-server-login-2.6.7.2.zip`.

---

## 🚀 Шаг 6. Запуск

### 🪟 Windows

```cmd
cd login
startLoginServer.bat
```

### 🐧 Linux / macOS

```bash
cd login
chmod +x startLoginServer.sh
./startLoginServer.sh
```

### 🔁 Автозапуск на Linux (systemd)

`/etc/systemd/system/l2j-login.service`:

```ini
[Unit]
Description=L2J Login Server
After=network.target mysql.service

[Service]
Type=simple
User=l2j
WorkingDirectory=/opt/l2j/login
ExecStart=/usr/bin/java -Xms512m -Xmx2G -jar /opt/l2j/login/l2jlogin.jar
Restart=on-failure
RestartSec=5
SuccessExitStatus=2

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now l2j-login
sudo journalctl -u l2j-login -f
```

---

## 🐳 Запуск в Docker

### Однострочный запуск

```bash
cd l2j-server-login
docker compose up --build
```

Что произойдёт:

1. 🐬 Поднимется MySQL 8 с автоинициализацией схемы из `login/sql/`.
2. 🔨 Соберётся образ LS из исходников.
3. 🚀 Запустится LS с пробросом портов 2106, 9014, 8080.

### Ручная сборка

```bash
cd l2j-server-login
docker build -t l2jserver/login:latest .
docker run --rm \
    -p 2106:2106 -p 9014:9014 -p 8080:8080 \
    -v $(pwd)/../login/config:/app/config:ro \
    -v $(pwd)/../login/data:/app/data \
    -v $(pwd)/log:/app/log \
    l2jserver/login:latest
```

### Health-check в Docker

```bash
docker inspect --format='{{.State.Health.Status}}' <container>
curl -fsS http://127.0.0.1:8080/health
```

---

## 🔒 Безопасность

### 🔑 Пароли (PBKDF2 + миграция)

- Новые пароли хранятся как `$pbkdf2-sha256$600000$<salt>$<hash>`.
- Старые пароли SHA-1 продолжают работать, **молча мигрируются** при логине.
- `ChangePassword` от Game Server тоже сохраняет PBKDF2.

**Политика паролей** (`PasswordPolicy`):

| Требование | Значение |
|------------|----------|
| Минимальная длина | 8 символов |
| Минимум классов символов | 2 (из: нижний регистр, верхний, цифра, символ) |
| Запрещённые | топ-20 самых популярных (`password`, `123456`, `qwerty`...) |

Применяется при **auto-create** и **ChangePassword**.

**Миграция неактивных legacy-аккаунтов**:

```
telnet 127.0.0.1 12345
pw-stats                    # покажет: сколько PBKDF2 vs legacy
pw-expire-legacy 90         # установит accessLevel=-100 для legacy,
                            # не логинившихся 90+ дней
```

---

### 🔐 TOTP 2FA

Совместимо с **Google Authenticator**, **Authy**, **Microsoft Authenticator**.

#### Включение для аккаунта (админ):

```
telnet 127.0.0.1 12345
totp-set <login>
```

Сервер выведет:

```
TOTP secret for <login>: JBSWY3DPEHPK3PXP
Enter in authenticator app. Login as 'password:123456'.
```

1. 📱 Введите секрет в Google Authenticator (вручную, тип Time-based).
2. ✅ Проверьте: приложение показывает 6-значный код, меняющийся каждые 30 секунд.

#### Вход игрока с TOTP:

В клиенте L2 игрок вводит в поле пароля:

```
<password>:<6_digit_code>
```

Пример: `MyPassw0rd:432716`

> 💡 Для обычных аккаунтов (без TOTP) формат пароля не меняется — точка с запятой может остаться в пароле, если она не совпадает с 6 цифрами в конце.

#### Отключение 2FA:

```
telnet> totp-clear <login>
```

---

### 🚫 CIDR-бан подсетей

Банить можно:

- 🎯 Точный IP (старый механизм).
- 🌐 Подсеть CIDR (новое): `a.b.c.0/24`, `2001:db8::/32`.

#### Через файл (`banned_ip.cfg`):

```
# Vpn-провайдер полностью
185.233.0.0/16
# Tor exit nodes одной подсети
192.42.116.0/24
```

Перезагрузить файл — перезапуск сервера.

#### Через telnet на живом сервере:

```
telnet> ban-subnet 10.0.0.0/24
Subnet 10.0.0.0/24 added to ban list.
```

---

### 🔐 TLS между LS и GS

Защищает канал между Login Server и Game Server'ами, если они на разных хостах.

#### Генерация keystore:

```bash
mkdir -p login/config/tls
keytool -genkeypair -alias ls \
    -keyalg RSA -keysize 4096 -validity 3650 \
    -storetype PKCS12 \
    -keystore login/config/tls/keystore.p12 \
    -storepass YOUR_STRONG_PASS \
    -dname "CN=l2j-login,OU=L2J,O=Server,C=UA"
```

#### Включение в `server.properties`:

```properties
GameServerTlsEnabled = True
GameServerTlsKeystore = config/tls/keystore.p12
GameServerTlsKeystorePassword = YOUR_STRONG_PASS
```

> ⚠️ Game Server должен подключаться как TLS-клиент и доверять сертификату LS. Если GS не модифицирован, оставьте `False`.

---

### 🛑 Блокировки и брутфорс

Три слоя защиты:

| Слой | Триггер | Действие |
|------|---------|----------|
| 🌐 Per-IP | `LoginTryBeforeBan` неудач с IP | Бан IP на `LoginBlockAfterBan` секунд |
| 👤 Per-account | 8 неудач на логин | Экспоненциальный lockout: 1 мин → 2 → 4 ... до 1 часа |
| 🚨 Flood | Слишком частые коннекты с IP | Discard до нормализации |

Разблокировка IP:

```
telnet> unblock 1.2.3.4
```

---

## 📊 Мониторинг

### 🩺 Health-check HTTP

Включается в `server.properties`:

```properties
HealthCheckEnabled = True
HealthCheckPort = 8080
```

Эндпоинты:

```bash
# Простой статус: 200 OK + "ok"
curl http://127.0.0.1:8080/health

# JSON с базовыми метриками
curl http://127.0.0.1:8080/metrics
# {"authedClients":12,"registeredGs":1,"bannedIps":5,"bannedSubnets":1}
```

Удобно для Docker healthcheck, Kubernetes liveness/readiness, balancer'ов.

---

### 📈 JMX-метрики

Для локального JMX никаких параметров не нужно — по умолчанию работает.

**Удалённый JMX** — добавьте в `startLoginServer.sh` перед `java`:

```bash
JMX_OPTS="-Dcom.sun.management.jmxremote \
          -Dcom.sun.management.jmxremote.port=9999 \
          -Dcom.sun.management.jmxremote.local.only=false \
          -Dcom.sun.management.jmxremote.authenticate=false \
          -Dcom.sun.management.jmxremote.ssl=false \
          -Djava.rmi.server.hostname=<ВАШ_IP>"
java $JMX_OPTS -jar l2jlogin.jar
```

> ⚠️ Не выставляйте JMX наружу без auth+TLS — это полный контроль над JVM.

**Просмотр**:

- `jconsole` (входит в JDK).
- VisualVM: https://visualvm.github.io/

MBean: `com.l2jserver.loginserver:type=Metrics`

| Атрибут | Описание |
|---------|----------|
| `LoginsOk` | Счётчик успешных логинов |
| `LoginsFail` | Счётчик неудачных логинов |
| `AccountLocks` | Сработавшие per-account lockouts |
| `PasswordMigrations` | Сколько паролей автомигрировано в PBKDF2 |
| `AuthedClients` | Активные логин-сессии прямо сейчас |
| `RegisteredGs` | Количество зарегистрированных GS |
| `BannedIpsCount` | Точных IP в ban-списке |
| `BannedSubnetsCount` | CIDR-подсетей в ban-списке |

---

### 📜 Audit-лог

Важные события пишутся в таблицу `login_audit` асинхронно, без блокировки.

События:

| Событие | Когда |
|---------|-------|
| `LOGIN_OK` | Успешный логин |
| `LOGIN_FAIL` | Неверный пароль |
| `ACCOUNT_LOCK` | Per-account lockout сработал |
| `PASSWORD_CHANGE` | Пароль изменён через ChangePassword |
| `ACCESS_LEVEL_CHANGE` | Access level изменён |
| `BAN` | Temp-бан применён |
| `GS_REGISTER` | Game server зарегистрирован |

**Просмотр**:

```sql
-- Последние 50 событий
SELECT FROM_UNIXTIME(ts/1000) AS time, event, account, ip, actor, details
FROM login_audit
ORDER BY id DESC
LIMIT 50;

-- Подозрительная активность (много неудач за сутки)
SELECT account, COUNT(*) AS fails
FROM login_audit
WHERE event = 'LOGIN_FAIL' AND ts > UNIX_TIMESTAMP(NOW() - INTERVAL 1 DAY) * 1000
GROUP BY account
HAVING fails > 20
ORDER BY fails DESC;

-- Все смены пароля за неделю
SELECT FROM_UNIXTIME(ts/1000), account, actor
FROM login_audit
WHERE event = 'PASSWORD_CHANGE'
  AND ts > UNIX_TIMESTAMP(NOW() - INTERVAL 7 DAY) * 1000;
```

Периодически чистите (после бэкапа):

```sql
DELETE FROM login_audit WHERE ts < UNIX_TIMESTAMP(NOW() - INTERVAL 90 DAY) * 1000;
```

---

### 📝 JSON-логи

Активировать в `log/log4j2.xml` (или `src/main/resources/log4j2.xml` пересобрать):

```xml
<Root level="info" additivity="false">
    <AppenderRef ref="console" />
    <AppenderRef ref="json-file" />  <!-- раскомментировать -->
</Root>
```

Файл: `log/loginserver.json` (ротация 50 МБ, 10 архивов).

Формат NDJSON:

```json
{"ts":"2026-04-19T10:15:33.421+03:00","level":"INFO","logger":"com.l2jserver.loginserver.LoginController","thread":"PacketHandler-1","msg":"...","exception":""}
```

Подходит для **Fluent Bit**, **Filebeat**, **Promtail**, **Vector**.

---

## 💻 Telnet-консоль

```bash
telnet 127.0.0.1 12345
```

После ввода пароля из `telnet.properties` — доступны команды:

| Команда | Описание |
|---------|----------|
| `help` | Список команд |
| `status` | Кол-во зарегистрированных GS |
| `unblock <ip>` | Снять IP с ban-листа |
| `ban-subnet <cidr>` | Добавить подсеть в ban-лист |
| `pw-stats` | Статистика паролей: PBKDF2 vs legacy |
| `pw-expire-legacy <days>` | Блокировать legacy-аккаунты, неактивные N дней |
| `totp-set <login>` | Включить 2FA для аккаунта |
| `totp-clear <login>` | Выключить 2FA |
| `RedirectLogger` | Перенаправить серверные логи в telnet-сессию |
| `shutdown` | Остановить сервер |
| `restart` | Перезапустить |
| `quit` / `exit` | Закрыть telnet |

**Rate-limit**: 5 неверных паролей с одного IP → lockout на 15 минут.

---

## 🧩 Подключение Game Server

### 1. Зарегистрировать GS в БД

Сгенерировать `hexid` можно через утилиту L2J Registration (часть GS) или вручную:

```sql
INSERT INTO gameservers (server_id, hexid, host)
VALUES (1, 'abc123def456...', '127.0.0.1');
```

### 2. Настройки в Game Server

В `game/config/LoginServer.properties` (или эквиваленте):

```properties
LoginHost = 127.0.0.1
LoginPort = 9014
LoginHexID = abc123def456...
LoginServerID = 1
```

### 3. Принимать новые GS при первом подключении

```properties
# server.properties
AcceptNewGameServer = True
```

> ⚠️ Оставьте `False` после регистрации всех ваших GS, иначе любой внешний клиент сможет зарегистрироваться как GS.

### 4. Запустить GS и проверить логи LS

```
[INFO ] ... Game Server 14 enabled.
[INFO ] ... GameServer [1] name is connected
```

---

## 🧯 Troubleshooting

### ❌ `JAVA_HOME is not defined correctly`

**Решение**: установите `JAVA_HOME`.

```bash
# Linux/macOS
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64

# Windows
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot" /M
# Перезапустить терминал
```

### ❌ `Address already in use: 2106`

```bash
# Linux
sudo ss -tlnp | grep 2106
sudo kill <pid>

# Windows
netstat -ano | findstr :2106
taskkill /PID <pid> /F
```

### ❌ `Communications link failure` (MySQL)

- Проверьте `URL`, `User`, `Password` в `database.properties`.
- Убедитесь, что MySQL слушает (`sudo ss -tlnp | grep 3306`).
- Если MySQL в Docker: имя хоста = `db` (не `localhost`).

### ❌ Клиент сразу дисконнектит

- Проверьте, что `banned_ip.cfg` не содержит IP клиента.
- Проверьте `LoginBlockAfterBan` — не забанен ли IP автоматически.
- Разблокировка: `telnet> unblock <ip>`.

### ❌ TOTP-код не принимается

- Часы сервера и устройства с Authenticator должны совпадать (±1 шаг = ±30 сек).
- Синхронизируйте время: `sudo timedatectl set-ntp true` (Linux) или «Sync now» в Authenticator.

### ❌ Game Server не может подключиться после `GameServerTlsEnabled=True`

- GS должен быть модифицирован под TLS-клиент.
- Если не модифицирован — временно `GameServerTlsEnabled = False`.

### ❌ `ERROR [...] GS X tried to change access level ... denied`

Это **ожидаемое поведение** нового trust boundary: GS пытается менять access level аккаунта, которого нет у него в онлайне. Если вы хотите разрешить — убедитесь, что игрок действительно залогинен на этом GS.

### ❌ `password is in the list of most common weak passwords`

Новая `PasswordPolicy` заблокировала слабый пароль. Используйте что-то посложнее.
Требования: 8+ символов, минимум 2 разных класса (буквы/цифры/символы).

---

## ❓ FAQ

**Q: Надо ли заставлять игроков менять пароли при переходе на PBKDF2?**
A: Нет. Миграция автоматическая, при первом успешном логине. Неактивные
аккаунты можно заэкспайрить: `pw-expire-legacy <days>`.

**Q: Как проверить, что TLS действительно работает между LS и GS?**
A: После старта LS с `GameServerTlsEnabled=True` выполните:
`openssl s_client -connect 127.0.0.1:9014 -showcerts`. Должен показаться ваш
сертификат.

**Q: Куда писать, если я хочу дисковый ratelimit на health-check?**
A: Поставьте nginx/traefik перед LS и настройте rate-limit там.
Встроенный HTTP-сервер JDK минималистичен.

**Q: Можно ли запустить LS и GS в одном docker-compose?**
A: Да, но образ GS в этом репо нет. Добавьте сервис `game` со своим
Dockerfile и пропишите `LoginHost = login` (имя сервиса compose).

**Q: Что делать, если потерял telnet-пароль?**
A: Задайте новый в `telnet.properties` и перезапустите LS.

**Q: Как бэкапить БД?**
A: Стандартно: `mysqldump -u l2j -p l2jdb > backup.sql`. Бэкапьте также
`login/config/`, если вносили правки.

**Q: Безопасно ли открывать `HealthCheckPort` наружу?**
A: Лучше оставить на `127.0.0.1`. Если нужен external mon — используйте
reverse-proxy с auth.

**Q: Где посмотреть, сколько аккаунтов на старом SHA-1?**
A: `telnet> pw-stats` или напрямую:
```sql
SELECT SUM(CASE WHEN password LIKE '$pbkdf2%' THEN 1 ELSE 0 END) AS new,
       SUM(CASE WHEN password NOT LIKE '$pbkdf2%' THEN 1 ELSE 0 END) AS legacy
FROM accounts;
```

---

## 📬 Дополнительно

- 📂 Исходники: `l2j-server-login/src/main/java/`
- 📂 SQL: `login/sql/` и `login/sql/migrations/`
- 📂 Конфиги runtime: `login/config/`
- 📂 Логи: `login/log/`
- 📂 Audit: таблица `login_audit` в БД

🆘 При проблемах соберите:

1. `login/log/` за последние сутки.
2. Свежий `SELECT * FROM login_audit ORDER BY id DESC LIMIT 100;`.
3. Вывод `curl -s http://127.0.0.1:8080/metrics` (если health-check включён).
4. Версию JDK: `java -version`.
5. Версию MySQL: `mysql --version`.

Удачной настройки! 🎮🛡️
