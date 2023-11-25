package com.ee364project;

import java.util.ArrayList;

import com.ee364project.helpers.Utilities;
import com.ee364project.helpers.Vars;

public class Problem implements HasData {
    private static ArrayList<Problem> allProblems = new ArrayList<>();
    private static final String CLSNAME = "Problem";
    private static final String[] HEADERS = new String[]{"identifier", "department", "customerIntro", "customerResponses", "agentIntro", "agentResponses"};
    public static final  Problem NO_PROBLEM = new Problem();

    private Department department;
    private String identifier;
    private String[] customerIntro;
    private String[] customerResponses;
    private String[] agentIntro;
    private String[] agentResponses;

    public Problem(String identifier, Department department, String[] customerIntro, String[] customerResponses, String[] agentIntro, String[] agentResponses) {
        this.identifier = identifier;
        this.department = department;
        this.customerIntro = customerIntro;
        this.customerResponses = customerResponses;
        this.agentIntro = agentIntro;
        this.agentResponses = agentResponses;
        allProblems.add(this);
    }

    public Problem() {
        this(Vars.NONE, Department.getRandomDepartment(), Vars.NONE2D, Vars.NONE2D, Vars.NONE2D, Vars.NONE2D);
    }

    @Override
    public String toString() {
        return Utilities.prettyToString(CLSNAME, this.identifier, this.department);
    }

    public Department getDepartment() {
        return this.department;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String[] getCustomerIntro() {
        return this.customerIntro;
    }

    public String[] getCustomerResponses() {
        return this.customerResponses;
    }

    public String[] getAgentIntro() {
        return this.agentIntro;
    }

    public String[] getAgentResponses() {
        return this.agentResponses;
    }

    @Override
    public String getDataTypeName() {
        return CLSNAME;
    }
    
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    @Override
    public String[] getData() {
        return new String[]{
            this.identifier,
            this.department.getName(),
            Utilities.joinStrings(this.customerIntro, ";"),
            Utilities.joinStrings(this.customerResponses, ";"),

            Utilities.joinStrings(this.agentIntro, ";"),
            Utilities.joinStrings(this.agentResponses, ";"),

        };
    }

    @Override
    public Problem parseData(String[] dataFields) {
        this.identifier = dataFields[0];
        this.department = Department.getDepartment(dataFields[1]);
        ArrayList<String> d1 = new ArrayList<>();
        ArrayList<String> d2 = new ArrayList<>();
        String[] ds = dataFields[2].split(";");
        for (int i = 0; i < ds.length; i++) {
            if (i % 2 == 0) {
                d2.add(ds[i]);
            } else {
                d1.add(ds[i]);
            }
        }
        this.customerResponses = d1.toArray(new String[d1.size()]);
        this.agentResponses = d2.toArray(new String[d2.size()]);

        this.customerIntro = dataFields[3].split(";");
        this.agentIntro = dataFields[4].split(";");
        return this;
    }

    @Override
    public Problem shuffle() {
        this.identifier = Utilities.faker.azure().appServiceEnvironment();
        this.department = Department.getRandomDepartment();
        this.customerIntro = Utilities.getRandomStringArray();
        this.customerResponses = Utilities.getRandomStringArray();
        this.agentIntro = Utilities.getRandomStringArray();
        this.agentResponses = Utilities.getRandomStringArray();
        return this;
    }


    public static Problem[] getAllProblems() {
        return allProblems.toArray(new Problem[allProblems.size()]);
    }
    
    // TODO: add problem related stuff like `state`. Note: Did that in ProblemState.
}
