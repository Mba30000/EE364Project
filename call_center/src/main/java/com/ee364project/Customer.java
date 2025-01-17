package com.ee364project;

import java.util.ArrayList;
import java.util.HashMap;

import com.ee364project.exceptions.InvalidPhoneNumberException;
import com.ee364project.helpers.*;


public class Customer extends Person implements CanCall {
    private static ArrayList<Customer> allCustomers = new ArrayList<>();
    public static Customer[] allCustomers() {
        return allCustomers.toArray(new Customer[allCustomers.size()]);
    }
    private static final String CLSNAME = "Customer";
    private static final String[] HEADERS = new String[]{"phone_number", "behaviour", "name"};

    private String phoneNumber;
    private CustomerBehaviour behaviour;
    public ProblemInfo problemState = new ProblemInfo();
    public CallInfo callInfo = new CallInfo();

    public Customer(String phoneNumber, CustomerBehaviour behaviour, String name) throws InvalidPhoneNumberException {
        super(name);
        if (!Utilities.validatePhone(phoneNumber)) {
            throw new InvalidPhoneNumberException(phoneNumber);
        }
        this.behaviour = behaviour;
        this.phoneNumber = phoneNumber;
        allCustomers.add(this);
    }

    public Customer() throws InvalidPhoneNumberException {
        this("0500000000", CustomerBehaviour.getRandomCustomerBehaviour(), Vars.NONE);
    }

    public Customer clone() {
        try { 
            return new Customer(this.phoneNumber, this.behaviour, this.getName());
        } catch (InvalidPhoneNumberException e) {
            return null;
        }
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        if (!Utilities.validatePhone(phoneNumber)) {
            throw new InvalidPhoneNumberException(phoneNumber);
        }
        this.phoneNumber = phoneNumber;
    }

    public CustomerBehaviour getBehaviour() {
        return this.behaviour;
    }

    public void setBehaviour(CustomerBehaviour behaviour) {
        this.behaviour = behaviour;
    }


    @Override
    public String toString() {
        return Utilities.prettyToString(CLSNAME, this.phoneNumber, this.behaviour, getName());
    }

    // related to data
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
            this.phoneNumber,
            this.behaviour.name,
            this.getName()
        };
    }
    public Customer parseData(String[] dataFields) {
        this.phoneNumber = dataFields[0];
        this.behaviour = CustomerBehaviour.customerBehaviourByName.get(dataFields[1]);
        this.setName(dataFields[2]);
        return this;
    }

    @Override
    public Customer shuffle() {
        this.setName(Utilities.faker.name().firstName());
        this.phoneNumber = "05" + Utilities.faker.number().digits(8);
        this.behaviour = CustomerBehaviour.getRandomCustomerBehaviour();
        return this;
    }

    @Override
    public void makeCall() {
        Call call = new Call(this);
        this.callInfo.called(call);
    }    
    private void idle(String msg) {
        Utilities.log(this, "idles", "", msg);
    }

    private void defaultRoutine() {
        if (this.problemState.isGotProblem()) {
            if (this.behaviour.callChance.check()) {
                makeCall();
                Utilities.log(this, "calls", "", "");
                return;
            } else {
                idle("");
                return;
            }
        } else {
            if (this.behaviour.problemAffinity.check()) {
                this.problemState.acquireProblem();
                Utilities.log(this, "got", this.problemState.getProblem(), "");
                return;
            } else {
                idle("");
                return;
            }
        }
    }
    @Override
    public void step() {
        if (this.callInfo.getLastCall() == null) {
            defaultRoutine();
            return;
        } 
        switch (this.callInfo.getLastCall().getState()) {
            case WAITING:
                idle("waiting for answer");
                return;
        
            case INCALL:
                if (this.problemState.isGotProblem()) {
                    if (this.behaviour.solveChancePartial.check()) {
                        this.problemState.solve();
                        Utilities.log(this, "got his", this.problemState.getProblem(), "solved.");
                    } else {Utilities.log(this, "idles", "", "");}
                } else {
                    if (this.behaviour.callEndChancePartial.check()) {
                        Call.endACall(this.callInfo.getLastCall());
                        Utilities.log(this, "ended", this.callInfo.getLastCall(), "");
                    } else {Utilities.log(this, "idles", "", "");}
                }
                return;

            default:
                defaultRoutine();
                return;
        }
    }

    // public void step() {
    //     // check if is in call
    //     if (this.callInfo.isInCall()) {
    //         // is in call right now.
    //         // let's check if the problem is solved
    //         if (this.problemState.isGotProblem()) {
    //             // has a problem.
    //             if (this.behaviour.solveChancePartial.check()) {  // FIXME: PARTIAL: used a partial as sole factor
    //                 // check passed problem solved.
    //                 this.problemState.solve();
    //             } else {
    //                 // idle
    //             }
    //         } else {
    //             // is in a call + the problem is solved = try to end the call
    //             if (this.behaviour.callEndChancePartial.check()) {  // FIXME: PARTIAL: used a partial as sole factor
    //                 Call.endACall(this.callInfo.getLastCall());
    //                 Utilities.log(this, "ended call", this.callInfo.getLastCall(), "");
    //             }
    //         }
    //     }
    //     if (this.problemState.isGotProblem()) {
    //         // has a problem but not in a call
    //         if (this.behaviour.callChance.check()) {
    //             // TODO: make a call
    //             Utilities.log(this, "calls", "UnknownForNow", "");;
    //         } else {
    //             // does not make a call
    //             if (this.call)
    //             Utilities.log(this, "idles", "", "");
    //         }
    //     } else {
    //         // does not have a problem
    //         if (this.behaviour.problemAffinity.check()) {
    //             // gets a problem
    //             this.problemState.acquireProblem();
    //             Utilities.log(this, "gets Problem->", this.problemState.getProblem(), "");
    //         } else {
    //             // gets to see another day :)
    //             Utilities
    //         }
    //     }
    // }

}


class CustomerBehaviour {
    public static HashMap<String, CustomerBehaviour> customerBehaviourByName = new HashMap<>();
    static {
        customerBehaviourByName.put(PreMade.DEFAULT.name, PreMade.DEFAULT);
        customerBehaviourByName.put(PreMade.SAVVY.name, PreMade.SAVVY);
        customerBehaviourByName.put(PreMade.CHALLENGED.name, PreMade.CHALLENGED);
    }
    public static CustomerBehaviour getRandomCustomerBehaviour() {
        return customerBehaviourByName.get(customerBehaviourByName.keySet().toArray()[Utilities.random.nextInt(customerBehaviourByName.size())]);
    }
    
    public static final class PreMade {
        public static final CustomerBehaviour DEFAULT = new CustomerBehaviour(
            "default",
            new Ratio(0.5),
            new Ratio(0.5),
            new Ratio(0.5),
            new Ratio(0.5)
            );
        public static final CustomerBehaviour SAVVY = new CustomerBehaviour(
            "savvy",
            new Ratio(0.1),
            new Ratio(0.2),
            new Ratio(0.5),
            new Ratio(0.7)
            );
            public static final CustomerBehaviour CHALLENGED = new CustomerBehaviour(
                "challenged",
                new Ratio(0.7),
                new Ratio(0.9),
                new Ratio(0.2),
                new Ratio(0.5)
                );
            }

    public String name;
    public Ratio problemAffinity;
    public Ratio callChance;
    public Ratio solveChancePartial;
    public Ratio callEndChancePartial;

    public CustomerBehaviour(String name, Ratio problemAffinity, Ratio callChance, Ratio solveChancePartial, Ratio callEndChancePartial) {
        this.name = name;
        this.problemAffinity = problemAffinity;
        this.callChance = callChance;
        this.solveChancePartial = solveChancePartial;
        this.callEndChancePartial = callEndChancePartial;
    }

    public CustomerBehaviour() {
        this(
            Utilities.faker.brand().toString(),
            Ratio.getRandRatio(),
            Ratio.getRandRatio(),
            Ratio.getRandRatio(),
            Ratio.getRandRatio()
        );
    }

    @Override
    public String toString() {
        return Utilities.prettyToString(
            "CBehaviour", 
            this.name
            // "problemAffinity=" + problemAffinity,
            // "callChance=" + callChance,
            // "solveChancePartial=" + solveChancePartial,
            // "callEndChancePartial=" + callEndChancePartial
        );
    }
}


class ProblemInfo {
    private HashMap<Problem, Long> history = new HashMap<>();
    private Problem problem = Problem.NO_PROBLEM;
    private long currentActiveProblemDuration = 0;

    private void finalizeLastProblem() {
        Long problemDuration = this.history.get(this.problem);
        if (problemDuration == null) {
            this.history.put(this.problem, this.currentActiveProblemDuration);
        } else {
            this.history.put(this.problem, problemDuration + this.currentActiveProblemDuration);
        }
    }

    public void solve() {
        finalizeLastProblem();
        this.problem = Problem.NO_PROBLEM;
        this.currentActiveProblemDuration = history.get(Problem.NO_PROBLEM);
    }

    public void acquireProblem() {
        finalizeLastProblem();
        this.problem = (Problem) Utilities.getRandomFromArray(Problem.getAllProblems());
        Long problemDuration = history.get(this.problem);
        if (problemDuration == null) {
            currentActiveProblemDuration = 0;
        } else {
            currentActiveProblemDuration = problemDuration;
        }
    }

    public Problem getProblem() {
        return this.problem;
    }

    public long getTotalProblemDuration() {
        long sum = 0;
        for (long i : history.values()) {
            sum += i;
        }
        return sum - history.get(Problem.NO_PROBLEM);
    }

    public void incrmentDuration() {
        this.currentActiveProblemDuration += Vars.TIMEINC;
    }

    public boolean isGotProblem() {
        if (this.problem != Problem.NO_PROBLEM) {
            return true;
        }
        return false;
    }
}

class CallInfo {
    private ArrayList<Call> history = new ArrayList<>();
    private Call call = null;

    public void called(Call call) {
        this.history.add(call);
        this.call = call;
    }

    public boolean isInCall() {
        return 
            (call.getState() == Call.CallState.INCALL)
            || (call.getState() == Call.CallState.WAITING);
    }

    public Call getLastCall() {
        return this.call;
    }
}