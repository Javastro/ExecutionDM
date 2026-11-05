Architecture
============


Component Diagram
-----------------

```plantuml format="svg_inline" 

component [OCI Registry] as OCI 
component [Application Metadata Store] as AMS
component [Execution Context Store] as ECS
component [Job Queue] as JQ
component [Data Processing Node] as DPN {

component Executor 
}
Executor --> OCI : Pulls container images
Executor --> AMS : Stores execution metadata
Executor --> ECS : Stores execution context and state
DPN --> Executor : Executes tasks on processing nodes
```

