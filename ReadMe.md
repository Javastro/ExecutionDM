ExecutionDM Project
===================

[![Java CI with Gradle](https://github.com/Javastro/ExecutionDM/actions/workflows/gradletest.yml/badge.svg)](https://github.com/Javastro/ExecutionDM/actions/workflows/gradletest.yml)

This project uses [VO-DML tools](https://ivoa.github.io/vo-dml/) to define a data model.

The source for the model is in [model/ExecutionDM-v1.vodsl](model/ExecutionDM-v1.vodsl)

```
gradle test
```
should build the model and run tests against it.
```shell
gradle testSite
```
should build the site and make it available at http://localhost:8000

