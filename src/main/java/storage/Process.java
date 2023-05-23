package storage;

import constants.Constants;

import java.io.Serializable;
import java.util.Vector;

public class Process implements Serializable {

    private PCB pcb;
    private Vector<UnParsedLine> unParsedLines;
    private Vector<Variable> variables;



    public Process(int id, Vector<UnParsedLine> unParsedLines, Vector<Variable> variables) {
        this.pcb = new PCB(id);
        this.unParsedLines = (Vector<UnParsedLine>) unParsedLines.clone();
        this.variables = (Vector<Variable>) variables.clone();
    }

    public Process(int id, Vector<UnParsedLine> unParsedLines) {
        this.pcb = new PCB(id);
        this.unParsedLines = (Vector<UnParsedLine>) unParsedLines.clone();
        variables = new Vector<>();
    }

    public Process(){}


    public PCB getPcb() {
        return pcb;
    }

    public Vector<UnParsedLine> getUnParsedLines() {
        return unParsedLines;
    }

    public Vector<Variable> getVariables() {
        return variables;
    }

    public int getID() {
        return pcb.getId();
    }

    public State getState() {
        return pcb.getState();
    }

    public void setState(State state) {
        pcb.setState(state);
    }

    public int getPC (){
        return pcb.getPc();
    }

    public void setPC(int pc){
        pcb.setPc(pc);
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public void setUnParsedLines(Vector<UnParsedLine> unParsedLines) {
        this.unParsedLines = (Vector<UnParsedLine>) unParsedLines.clone();
    }

    public void setVariables(Vector<Variable> variables) {
        this.variables = (Vector<Variable>) variables.clone();
    }

    public void inccrPC(){
        pcb.setPc(pcb.getPc()+1);
    }

    public MemoryBoundry getMemoryBoundry(){
        return pcb.getMemoryBoundry();
    }

    public int getsize(){
        return Constants.PCB_SIZE+ unParsedLines.size()+ variables.size();
    }

}
