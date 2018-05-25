package com.autodownloadimages.model;

/**
 * Created by Sunil kumar yadav on 14/2/18.
 */

public class RecipeModel extends BaseModel {

    String name;
    String material;
    String formula;
    String type_id;
    String row_id;
    String imageUrl;
    boolean isFavorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getRow_id() {
        return row_id;
    }

    public void setRow_id(String row_id) {
        this.row_id = row_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecipeModel) {
            RecipeModel recipeModel = (RecipeModel) obj;
            if (this.row_id.equalsIgnoreCase(recipeModel.getRow_id())) {
                return true;
            }
        }
        return false;
    }
}
