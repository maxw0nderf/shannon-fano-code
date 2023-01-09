package com.mwgroup.shannonfanocode;

public class Items {
    private String sym;
    private double ver;
    private String code;

    public Items(String sym, double ver, String code) {
        this.sym = sym;
        this.ver = ver;
        this.code = code;
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

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-20s %-5s %n", sym, ver, code);
    }
}
