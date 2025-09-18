# GEORGII ORLOV Java Dev

[Telegram](https://t.me/Ezh_v_tumane_ke) | [orlov.g.s@outlook.com](mailto:orlov.g.s@outlook.com) | [LinkedIn](https://www.linkedin.com/in/georgy-orlov-b25b1778/) | [StackOverflow](https://stackoverflow.com/users/9013930) | [GitHub](https://github.com/Ezhvtumane)

Professional backend developer with 8+ years of experience in Java and Kotlin, DevOps tools, and UGC mobile apps in
fintech companies. Bread-minded technical specialist with strong knowledge of data networks, SQL, and databases. Like a
leader managed the integration work with 3rd party service and the project team. Prefer to take a complex approach to
tasks and workflow, delve into the essence of the task and try to find the optimal solution before starting to write the
code. My main goal is to make our product better. The team player who is ready to be the first to help colleagues. An
attentive and supportive mentor for new team members.

### Tech:

Java, Kotlin, SQL, Spring Boot, Hibernate, AWS, PostgreSQL, MongoDB, RabbitMQ, Kafka, GraphQL, Redis, Docker, Wildfly,
Git, Nexus, Jenkins, Linux.

### Career history:

### Surf.dev, Java Backend Developer (outsource). 2023-02 2024-04

Key projects:

#### 1. Money transfer service & E-com service.

- **tech:** Java, Spring-boot, Hibernate, Postgres, Docker, Keycloak, mvn.
- Worked as a backend team lead(3 devs). Service based architecture(a few containers). Integration with a few customer
  services by API, also with payment service. I had been reviewing PR, task decomposition, set up the ci/cd processes,
  discussing technical topics with customer.

#### 2. A service for short-term rental of individual vehicles(IoT). High loads(>1000prs).

- **tech:** Java, Spring-boot, Hibernate, Postgres, Docker, AWS, Redis, Kafka, mvn.
- Feature of accounting for the work performed by operators. There is a problem: unknown actual amount of work was
  performed. Decided to count the results of each specific operator's work, also check the success of performed work.
  The statistics of completed works are written in the database. Payments to employees become to be made by that
  statistic. Analytics are made by that statistic to predict work.
- I have redesigned the integration of employee accounting from a third-party system (from the pull model to push), the
  integration has become more stable, the number of incidents has decreased, and the load on the database has decreased.
- Worked as a feature lead. Feature of forecasting workload. Interaction with related departments(ML, Frontend, QA, BA).
  The work schedule and workload of the workers have become more transparent for the workers themselves and the
  management. Automatic assignment of workers to jobs using a predictive model.

#### 3. Medical information system.

- **tech:** Kotlin, Spring-boot, Spring-cloud, Hibernate, Postgres, Keycloak, Gradle, GraphQL.
- I quickly joined the project and started integration with a third-party system through the GraphQL client. I found a
  bug in the apollographql/apollo-kotlin library. Made a ticket on github - discussed it with the lib developers and the
  bug was fixed in the next version of the library.

### Swoo App, Java Backend Developer (UGC, Fintech). 2022-02 2023-02

Development of a backend for the mobile application - a digital wallet that organizes discount and loyalty cards and
stores bank card data.

**Tech:** Java, Kotlin, Spring-boot, Spring-cloud, Hibernate, Postgres, AWS, Docker, mvn.

#### Key achievements(укоротить):

- Added user personal documents/ids (DL, Passport, etc.) to the application as a separate new entity that gave our users
  to securely use their personal documents in the app. Successfully fixed a lot of legacy code;
- Refactored the user authorization service (and several other small services involved) that created an opportunity to
  make new features faster and safer in the future. Divided service for a few modules (API, App, Client);
- Refactored all the services responsible for the moderation of loyalty cards. Brought the code to the "as in production
  environment" state (there was a difference between the versions of the code in the git and in the production). It
  opened the opportunity to make new features and have similar test and production environments;
- Written several AWS lambda with a more experienced developer and shared my experience with the backend devs team;

### SberTech, Java Developer (DevOps, Fintech). 2016-11 2022-02

Developed the DevOps tool. Made integrations with Jenkins and Nexus. This tool helps teams to automate and visualise the
release process of any project in the Sber ecosystem. Quickly and efficiently deliver products to production.

**Tech:** Java, Kotlin, Spring-boot, Spring-cloud, Hibernate, Postgres, GraphQL, Wildfly, Jenkins, Docker, mvn.

#### Key achievements:

- Improved microservice to work with Jenkins. Created more convenient and safer for using code. Connections have stopped
  “leaking".
- Implemented new functionality that allows executing Jenkins input stages inside of our product. The product became
  more convenient for customers and they did not need to open Jenkins for input steps use.
- Managed the integration of our product with the service of checking the quality gates. Created common API and models.
  Led the integration work. Made a task plan, carried out planning, and coordinated the work of teammates.

#### EDUCATION & TRAINING:

- Async architecture. [CodeNest LLC](https://codenest.school/). 2023.
- JVA-043 Spring Cloud for Java-devs, «Luxsoft training center», Moscow, 2020.
- JVA-013 Spring Security for Java-devs, «Luxsoft training center», Moscow, 2020.
- Master’s degree in secured communication systems. The Vyatka State University, Kirov, Russia, 2011.