package org.ivoa.dm.execution;
/*
 * Created on 10/05/2023 by Paul Harrison (paul.harrison@manchester.ac.uk).
 */

import jakarta.persistence.criteria.CriteriaBuilder;
import org.ivoa.dm.tool.Author;
import org.ivoa.vodml.testing.AutoDBRoundTripTest;
import org.ivoa.vodml.testing.AutoRoundTripWithValidationTest;

import java.util.List;

/**
 * This will run an XML and JSON round trip test on the model inst
 */
public class ExecutionModelTest extends AutoDBRoundTripTest<ExecutionModel, Long, ExecutionNode> { //TODO executionNode is too low level to do a good test - replace with something higher level - for now this is just testing whether JPA ok

   BaseExample bm = new BaseExample();

    @Override
    public ExecutionModel createModel() {
        return bm.createModel();
    }

    @Override
    public void testModel(ExecutionModel executionModel) {
        //this could do specialized testing on the model instance
    }

    @Override
    public ExecutionNode entityForDb() {
        return bm.jboNode;
    }

    @Override
    public void testEntity(ExecutionNode e) {
       // do something
    }
}
