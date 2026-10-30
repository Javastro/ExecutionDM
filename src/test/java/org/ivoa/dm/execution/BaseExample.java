package org.ivoa.dm.execution;


import org.ivoa.dm.tool.Author;

import java.util.List;

public class BaseExample {

   ExecutionNode jboNode;

   public ExecutionModel createModel() {
      // create the model instance here.
      ExecutionModel retval = new ExecutionModel();
      Author author = Author.createAuthor(a -> {
         a.name = "Paul Harrison";
         a.email = "paul.harrison@manchester.ac.uk";
         a.github = "pahjbo";
      });

      PhysicalLocation jbo = new PhysicalLocation("JBO", "UK", 53.2361, +2.3056);
      PhysicalLocation cam = new PhysicalLocation("Cambridge", "UK", 52.20, -0.13);

      ComputeResource small = ComputeResource.createComputeResource(n -> {
         n.name = "Small Compute Resource";
         n.memory = 4.0;
         n.numberOfCores = 2;
      });
      ComputeResource medium = ComputeResource.createComputeResource(n -> {
         n.name = "Medium Compute Resource";
         n.memory = 8.0;
         n.numberOfCores = 4;
      });
      ComputeResource large = ComputeResource.createComputeResource(n -> {
         n.name = "Large Compute Resource";
         n.memory = 16.0;
         n.numberOfCores = 15;
         n.gpu = true;
      });

      jboNode = ExecutionNode.createExecutionNode(n -> {
         n.name = "JBO Node";
         n.location =    jbo;
         n.computeResources = List.of(small, medium, large);
      });

      ExecutionNode camNode = ExecutionNode.createExecutionNode(n -> {
         n.name = "Cam Node";
         n.location =    cam;
         n.computeResources = List.of(small, medium);

      });
      return retval;
   }
}
