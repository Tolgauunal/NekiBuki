package com.bilmece.nekibuki.Categories;

public class Model  {
    private String title,code,soru;
    private int img;
    boolean isSelect;
    String color;
    boolean allSelect;

    public boolean isAllSelect() {
        return allSelect;
    }

    public void setAllSelect(boolean allSelect) {
        this.allSelect = allSelect;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSoru() {
        return soru;
    }

    public void setSoru(String soru) {
        this.soru = soru;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
