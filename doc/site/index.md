ExecutionDM Home
==================

The execution data model (ExecutionDM) is a data model for describing the execution of computational tasks. It is
designed to be used in the context of the Virtual Observatory (VO) and is based on the VO-DML modelling language.

It has been influenced by other IVOA activities such as [UWS](https://www.ivoa.net/documents/UWS/) (which itself was
an interface extracted from part of AstroGrid's [Common Execution Architecture](https://www.ivoa.net/Documents/Notes/CEA/CEADesignIVOANote-20050513.html). 
Recently the IVOA has worked on the [ExcutionBroker](https://github.com/ivoa-std/ExecutionBroker) which is also the specification for describing the execution of
computational tasks in a distributed environment. 

The ExecutionDM and associated tools and services can take advantage in the widespread use of containerisation and
orchestration technologies such as Docker and Kubernetes, which allow for the efficient and scalable execution of
computational tasks in a distributed environment. It should be noted that these were not available when CEA and UWS were developed. The
restriction to being "opinionated" about the containerisation has been inspired
by [OpenCADC Library Tools](http://www.opencadc.org/library-tools/) and the containerised "application" approach allows
the execution to be decoupled from the underlying infrastructure.  The project considers the functionality inside the 
container as a "black box" - the tools developed in this project are concerned only with the inputs and outputs of the "black box".

The data model is designed to be used as a basis for the development of APIs and services that allow for the execution of computational tasks in a distributed environment. The various services such as job queues, schedulers and resource brokers can be developed based on the data model, and the data model can be used to define the semantics of the execution environment. 
Different implementations of these services can be developed based on the same data model, allowing for interoperability and flexibility in the execution environment, as well as allowing for the development of a rich ecosystem of tools and services that can be used to execute computational tasks in the VO context.

More details about the initial design motivations can be found in the [requirements document](prd.md).
