package com.mwgroup.shannonfanocode;

public class PreItems {
    private String sym;
    private double ver;

    public PreItems(String sym, double ver) {
        this.sym = sym;
        this.ver = ver;
    }

    public String getSym() {
        return sym;
    }
    public void setSym(String sym) {
        this.sym = sym;
    }

    public double getVer() {
        return ver;
    }
    public void setVer(double ver) {
        this.ver = ver;
    }

    @Override
    public String toString() {
        return String.format("%-3s %-3s", sym, ver);
    }
}
