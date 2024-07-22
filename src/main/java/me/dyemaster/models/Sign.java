package me.dyemaster.models;

import com.github.retrooper.packetevents.util.Vector3i;

public class Sign {
    private int signType;//0:none/1:name/2:color
    private Vector3i signVec;

    public Sign(int signType) {
        this.signType = signType;
    }

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    public Vector3i getSignVec() {
        return signVec;
    }

    public void setSignVec(Vector3i signVec) {
        this.signVec = signVec;
    }
}
