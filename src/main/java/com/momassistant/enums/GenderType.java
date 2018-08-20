package com.momassistant.enums;

/**
 * Created by zhufeng on 2018/8/20.
 */
public enum GenderType {
    MALE(1, "男"), FEMALE(2, "女"),;
    private int gender;
    private String genderTxt;
    GenderType(int gender, String genderTxt) {
        this.gender = gender;
        this.genderTxt = genderTxt;
    }

    public int getGender() {
        return gender;
    }

    public String getGenderTxt() {
        return genderTxt;
    }

    public static GenderType getByType(int gender) {
        if (gender == GenderType.MALE.gender) {
            return GenderType.MALE;
        }
        return GenderType.FEMALE;
    }
}
