# Testerra demos

This project demonstrates different features of [Testerra 2 framework]([https://github.com/telekom/testerra]).

For a simple skeleton project please have a look to https://github.com/telekom/testerra-skeleton.

For simplicity, we will use Gradle as build and dependency management tool. 

## Perquisites

**Note:** All you have to do is, to set up an [own selenium server](http://docs.testerra.io/testerra/2-latest/index.html#_setup_selenium)

## What this demo project offers you

This project contains several subprojects as examples.

| Subproject         | Description                                                                                           |
|--------------------|-------------------------------------------------------------------------------------------------------|
| *minimal*          | The absolute minimal example for performing a Web based test.                                         |
| *page-objects*     | A minimal example using the page objects pattern                                                      |
| *layout-check*     | A simple example how to check layouts.                                                                |
| *inject-factories* | Shows how to inject your own page factory as a technical example.                                     |
| *the-internet*     | More complex practical examples from Testerra 1 skeleton.                                             |
| *selenium4-bidi*   | Demonstrate using the Selenium 4 BiDi featuress like WebDriver BiDi and ChromeDeveloperTools support. | 

## Execution

Navigate to the folder of the subproject and run

````shell
gradle test
````
or open the project in your IDE and run the files under `src/test` of the respective project.

## Code of Conduct

This project has adopted the [Contributor Covenant](https://www.contributor-covenant.org/) in version 2.0 as our code of conduct. Please see the details in our [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md). All contributors must abide by the code of conduct.

## Working Language

We decided to apply _English_ as the primary project language.

Consequently, all content will be made available primarily in English. We also ask all interested people to use English as language to create issues, in their code (comments, documentation etc.) and when you send requests to us. The application itself and all end-user faing content will be made available in other languages as needed.

## Support and Feedback
The following channels are available for discussions, feedback, and support requests:

| Type                     | Channel                                                |
| ------------------------ | ------------------------------------------------------ |
| **Issues**   | <a href="https://github.com/telekom/testerra-skeleton/issues/new/choose" title="Issues"><img src="https://img.shields.io/github/issues/telekom/testerra-skeleton?style=flat"></a> |
| **Other Requests**    | <a href="mailto:testerra@t-systems-mms.com" title="Email us"><img src="https://img.shields.io/badge/email-CWA%20team-green?logo=mail.ru&style=flat-square&logoColor=white"></a>   |

## How to Contribute

Contribution and feedback is encouraged and always welcome. For more information about how to contribute, the project structure, as well as additional contribution information, see our [Contribution Guidelines](./CONTRIBUTING.md). By participating in this project, you agree to abide by its [Code of Conduct](./CODE_OF_CONDUCT.md) at all times.

## Contributors

At the same time our commitment to open source means that we are enabling -in fact encouraging- all interested parties to contribute and become part of its developer community.

## Licensing

Copyright (c) 2022 Deutsche Telekom MMS GmbH.

Licensed under the **Apache License, Version 2.0** (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at https://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the [LICENSE](./LICENSE) for the specific language governing permissions and limitations under the License.
