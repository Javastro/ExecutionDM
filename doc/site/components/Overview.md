Software Components [^1]
===================

Although the ExecutionDM is a data model that is designed to enable a set of interoperable services, it is not a service specification in itself. The ExecutionDM is obviously based on some concept of their being some concrete services that implement the data model, but the data model itself is agnostic to the actual implementation of those services. Broadly speaking there are some core components that are required to implement a minimal viable set of services that make sense in the context of the ExecutionDM, and there are some possible extensions that could be added to the core components to provide additional functionality and possible service topologies. The following sections describe these core components and possible extensions;

## Core Components

The core components are listed below with a minimal description of their role in the overall system. It is expected that the core components might well be implemented as a single service, or as a set of separate services that communicate with each other. The important point is that the core components are required to implement a minimal viable set of services that make sense in the context of the ExecutionDM. 
There might well be extensions to the functionality of the core components to allow them to interact with the extension components, but the initial expectation is that the core components should be able to function independently of the extension components.

### Executor Service

This is the fundamental core service that would exist in any implementation of the ExecutionDM. It is responsible for accepting job requests, and returning results to the user. The Executor Service would be responsible for managing the lifecycle of jobs, including starting, stopping, and monitoring their progress. In addition an important role of the Executor Service is to manage the execution environment, including the allocation of resources, and the management of input and output data. 
The Executor Service would be responsible for ensuring that the correct container image is used for the execution of the job, and that the correct input and output files are mounted into the container, as described in the [runtime environment](../runtimeEnvironment.md). 

### Application Registry Service

The Application Registry Service is responsible for maintaining a registry of available applications that can be executed by the Executor Service. 
This service would provide a way for users to discover available applications, and to register new applications with the system. The Application Registry Service would also be responsible for managing the metadata associated with each application, including the container image, input and output data requirements, and any other relevant information.

## Extension Components

In general the core services can be thought of as being able to work in a "single node" environment, where all the services are running on a single machine. However in a more complex environment, where there are multiple nodes, and possibly multiple execution environments, it is expected that there would be additional services that would be required to manage the complexity of the system. The following sections describe some possible extension components that could be added to the core components to provide additional functionality and possible service topologies.

### Job Queue Service

This is a centralised service that is responsible for managing the queue of jobs that are waiting to be executed. Executor services, when idle, could examine the job queue to determine which jobs fit their local capabilities and then pull those jobs from the queue for execution. The Job Queue Service would be responsible for managing the lifecycle of jobs in the queue, including the adding new jobs, removing completed jobs, and monitoring the progress of jobs in the queue.

### Orchestrator Service

This service takes the concept of a job queue service one step further. The Orchestrator Service would be a framework for managing multiple job queue services, and would be responsible for routing jobs to the appropriate job queue service based on the requirements of the job and the capabilities of the available job queue services. The Orchestrator Service would also add the possibility of "booking" jobs on specific execution platforms, and would be responsible for managing the lifecycle of jobs across multiple job queue services. 

### Execution Platform Registry Service

To support the broker service, and to some extent even the Job execution service (if efficient data movement is to be supported), it is expected that there would be a need for a registry of available execution platforms. The Execution Platform Registry Service would be responsible for maintaining a registry of available execution platforms, and for providing a way for users to discover available execution platforms, and to register new execution platforms with the system. The Execution Platform Registry Service would also be responsible for managing the metadata associated with each execution platform, including the capabilities of the platform, and any other relevant information.

### Workflow Service

This service would be responsible for managing the execution of complex workflows that consist of multiple jobs. The Workflow Service would be responsible for managing the dependencies between jobs, and for ensuring that jobs are executed in the correct order. The Workflow Service would also be responsible for managing the lifecycle of workflows, including starting, stopping, and monitoring their progress.

### GUIs

* Given that the ExecutionDM has a general model of the inputs and outputs of a job, it is possible to create a generic GUI that can be used to submit jobs to the Executor Service, and allow users to search for the available applications in the Application Registry Service. The GUI could also provide a way for users to monitor the progress of their jobs, and to retrieve the results of completed jobs.
* The application registry service could provide a way for users to discover available applications, and to register new applications with the system via GUIs.

[^1]: There is some overlap between the content of this page and that of [architecture.md](../architecture.md). 