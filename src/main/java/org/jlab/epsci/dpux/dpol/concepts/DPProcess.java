package org.jlab.epsci.dpux.dpol.concepts;

public class DPProcess {
    private String processName;
    private boolean isSync;
    private boolean isPeriodic;
    private int period;
    private String transition;
    private boolean isBefore;
    private boolean isAt;
    private boolean isAfter;
    private String scriptCommand;
    private int exitCode;
    private String sendSubject;
    private String sendType;
    private String sendText;
    private boolean isSendRc;
    private String receiveSubject;
    private String receiveType;
    private String receiveText;
    private boolean isReceiveRc;
    private boolean isInitiator;

    public DPProcess(String processName) {
        this.processName = processName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public boolean isPeriodic() {
        return isPeriodic;
    }

    public void setPeriodic(boolean periodic) {
        isPeriodic = periodic;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public boolean isBefore() {
        return isBefore;
    }

    public void setBefore(boolean before) {
        isBefore = before;
    }

    public boolean isAt() {
        return isAt;
    }

    public void setAt(boolean at) {
        isAt = at;
    }

    public boolean isAfter() {
        return isAfter;
    }

    public void setAfter(boolean after) {
        isAfter = after;
    }

    public String getScriptCommand() {
        return scriptCommand;
    }

    public void setScriptCommand(String scriptCommand) {
        this.scriptCommand = scriptCommand;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public String getSendSubject() {
        return sendSubject;
    }

    public void setSendSubject(String sendSubject) {
        this.sendSubject = sendSubject;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendText() {
        return sendText;
    }

    public void setSendText(String sendText) {
        this.sendText = sendText;
    }

    public boolean isSendRc() {
        return isSendRc;
    }

    public void setSendRc(boolean sendRc) {
        isSendRc = sendRc;
    }

    public String getReceiveSubject() {
        return receiveSubject;
    }

    public void setReceiveSubject(String receiveSubject) {
        this.receiveSubject = receiveSubject;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getReceiveText() {
        return receiveText;
    }

    public void setReceiveText(String receiveText) {
        this.receiveText = receiveText;
    }

    public boolean isReceiveRc() {
        return isReceiveRc;
    }

    public void setReceiveRc(boolean receiveRc) {
        isReceiveRc = receiveRc;
    }

    public boolean isInitiator() {
        return isInitiator;
    }

    public void setInitiator(boolean initiator) {
        isInitiator = initiator;
    }

    @Override
    public String toString() {
        return "DPProcess{" +
                "processName='" + processName + '\'' +
                ", isSync=" + isSync +
                ", isPeriodic=" + isPeriodic +
                ", period=" + period +
                ", transition='" + transition + '\'' +
                ", isBefore=" + isBefore +
                ", isAt=" + isAt +
                ", isAfter=" + isAfter +
                ", scriptCommand='" + scriptCommand + '\'' +
                ", exitCode=" + exitCode +
                ", sendSubject='" + sendSubject + '\'' +
                ", sendType='" + sendType + '\'' +
                ", sendText='" + sendText + '\'' +
                ", isSendRc=" + isSendRc +
                ", receiveSubject='" + receiveSubject + '\'' +
                ", receiveType='" + receiveType + '\'' +
                ", receiveText='" + receiveText + '\'' +
                ", isReceiveRc=" + isReceiveRc +
                ", isInitiator=" + isInitiator +
                '}';
    }
}
