package com.autodownloadimages.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.autodownloadimages.model.MenuInfoModel;
import com.autodownloadimages.model.RecipeModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Sunil kumar yadav on 14/2/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static String DB_NAME = "recipe.sqlite";
    private static String FAVORITE_TABLE = "favorite_item";
    private static String DB_PATH = "";
    private final Context mContext;
    private SQLiteDatabase mDataBase;
    private boolean mNeedUpdate = false;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME,
                null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

    public boolean addFavorite(boolean isFavorite, RecipeModel recipeModel) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        values.put("name", recipeModel.getName());
        values.put("material", recipeModel.getMaterial());
        values.put("formula", recipeModel.getFormula());
        values.put("type_id", recipeModel.getType_id());
        values.put("row_id", recipeModel.getRow_id());
        values.put("imageUrl", recipeModel.getImageUrl());
        if (recipeModel.getIsFavorite()) {
            values.put("isFavorite", "1");
        } else {
            values.put("isFavorite", "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();
        if (!isFavorite) {
            createSuccessful = db.insert(FAVORITE_TABLE, null, values) > 0;
        } else {
            String whereClause = "type_id=? AND row_id=?";
            String[] whereArgs = new String[]{String.valueOf(recipeModel.getType_id()),
                    String.valueOf(recipeModel.getRow_id())};
            Log.e(TAG, "updateRecipeModel: "+recipeModel.getType_id() );
            createSuccessful = db.delete(FAVORITE_TABLE, whereClause, whereArgs) > 0;
        }
        db.close();

        return createSuccessful;
    }

    public boolean updateRecipeModel(RecipeModel recipeModel) {
        String Table_Name = "recipe";
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        if (recipeModel.getIsFavorite()) {
            values.put("isFavorite", "1");
        } else {
            values.put("isFavorite", "0");
        }

        String whereClause = "type_id=? AND row_id=?";
        String[] whereArgs = new String[]{String.valueOf(recipeModel.getType_id()),
                String.valueOf(recipeModel.getRow_id())};
        SQLiteDatabase db = this.getReadableDatabase();
        createSuccessful = db.update(Table_Name, values, whereClause, whereArgs) > 0;
        db.close();

        return createSuccessful;
    }

    public List<MenuInfoModel> getMenuInfoDetail() {
        String Table_Name = "menu_info";

        String selectQuery = "SELECT * FROM " + Table_Name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<MenuInfoModel> data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                MenuInfoModel model = new MenuInfoModel();
                model.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
                model.setId(cursor.getInt(cursor.getColumnIndex("id")));
                model.setName(cursor.getString(cursor.getColumnIndex("name")));
                data.add(model);
            } while (cursor.moveToNext());
        }
        db.close();
        return data;
    }

    public List<RecipeModel> getRecipes(int menuId) {
        String Table_Name = "recipe";

        String selectQuery = "SELECT * FROM " + Table_Name + " where type_id=" + String.valueOf(menuId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<RecipeModel> data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                RecipeModel model = new RecipeModel();
                model.setName(cursor.getString(cursor.getColumnIndex("name")));
                model.setMaterial(cursor.getString(cursor.getColumnIndex("material")));
                model.setFormula(cursor.getString(cursor.getColumnIndex("formula")));
                model.setType_id(cursor.getString(cursor.getColumnIndex("type_id")));
                model.setRow_id(cursor.getString(cursor.getColumnIndex("row_id")));
                model.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
                String isFavorite = cursor.getString(cursor.getColumnIndex("isFavorite"));
                if (isFavorite.equals("1")){
                    model.setIsFavorite(true);
                }else {
                    model.setIsFavorite(false);
                }
                        data.add(model);
            } while (cursor.moveToNext());
        }
        db.close();
        return data;
    }

    public List<RecipeModel> getFavorite() {
        String selectQuery = "SELECT * FROM " + FAVORITE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<RecipeModel> data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                RecipeModel model = new RecipeModel();
                model.setName(cursor.getString(cursor.getColumnIndex("name")));
                model.setMaterial(cursor.getString(cursor.getColumnIndex("material")));
                model.setFormula(cursor.getString(cursor.getColumnIndex("formula")));
                model.setType_id(cursor.getString(cursor.getColumnIndex("type_id")));
                model.setRow_id(cursor.getString(cursor.getColumnIndex("row_id")));
                model.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
                model.setIsFavorite(true);
                data.add(model);
            } while (cursor.moveToNext());
        }
        db.close();
        return data;
    }
}