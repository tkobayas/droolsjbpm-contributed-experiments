package org.drools.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.mvel2.MVEL;
import org.mvel2.compiler.ExpressionCompiler;

public class WorkItemNotification {
    private String workItemExp;
    private WorkItemHandler workItemHandler;
    private String docVar = "doc";

    public void executeWorkItem(Task task,
                                Notification notification,
                                List<OrganizationalEntity> recipients,
                                List<OrganizationalEntity> businessAdministrators,
                                WorkItemManager workItemManager,
                                EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        
        TaskData taskData = task.getTaskData();
        
        // First compile the associated document, we assume this returns a single structure
        // That can be used in the main work item evaluation.
        Content content = null;
        if ( taskData.getDocumentAccessType() == AccessType.Inline ) {
            content = em.find( Content.class,
                               taskData.getDocumentContentId() );
        }
        ExpressionCompiler compiler = new ExpressionCompiler( new String( content.getContent() ) );
        Serializable expr = compiler.compile();
        Object object = MVEL.executeExpression( expr );
        
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put( docVar, object );
        // for now will have to assume the recipient is a User, we need to figure out if Groups have an alias
        // of if we list all the individuals in the gruop.
        for ( OrganizationalEntity recipient : recipients  ) {
            vars.put( "user", recipient );
            WorkItem workItem = (WorkItem) MVEL.executeExpression( expr, vars );
            workItemHandler.executeWorkItem( workItem, workItemManager );
        }
        



        

    }
}