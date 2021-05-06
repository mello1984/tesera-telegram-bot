Webhook Telegram-бот на Java для мониторинга https://tesera.ru/, для размещения в Oracle Cloud VPS. Структура страниц
сайта не меняется, поэтому работа ведется просто через GET-запросы и обработку ответов с помощью RestTemplate (втч XML).

Используется Webhook-бот с самоподписанным сертификатом, белый ip-адрес обеспечен инфраструктурой ORACLE Cloud

Отслеживаемые типы публикаций на портале:
- Новость 
- Статья
- Игровой дневник
- Мысль
- Комментарии к играм, а также вышеперечисленным типам публикаций

Бот позволяет настраивать подписку:
- Отдельно к новостям, статьям, дневникам, мыслям и комментариям к ним 
- К комментариям к играм 
- Тонкая настройка комментариев к интересующему списку игр

Данные по подпискам сохраняются в БД с помощью Spring Data JPA

Стек технологий:
- Java 11
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Gradle
- Heroku
