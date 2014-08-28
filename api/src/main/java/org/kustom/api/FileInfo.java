package org.kustom.api;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.MatrixCursor;

import java.io.File;

public class FileInfo {
    protected static final String COL_VALID = "valid";
    protected static final String COL_SIZE = "size";
    protected static final String COL_MODIFIED = "modified";

    private boolean mIsValid = false;
    private long mSize = 0L;
    private long mModified = 0L;

    protected FileInfo(Cursor c) {
        if (c != null) try {
            c.moveToFirst();
            mIsValid = Boolean.parseBoolean(c.getString(c.getColumnIndexOrThrow(COL_VALID)));
            mSize = Long.valueOf(c.getString(c.getColumnIndexOrThrow(COL_SIZE)));
            mModified = Long.valueOf(c.getString(c.getColumnIndexOrThrow(COL_MODIFIED)));
            c.close();
        } catch (CursorIndexOutOfBoundsException e) {
            Logger.d("Cursor is empty, file not found");
        } catch (Exception e) {
            Logger.e("Invalid cursor data for File Info", e);
        }
    }

    protected static Cursor buildCursor(boolean isValid, File sourceFile) {
        MatrixCursor cursor = new MatrixCursor(new String[]{
                COL_VALID,
                COL_SIZE,
                COL_MODIFIED
        });
        cursor.addRow(new String[]{
                Boolean.toString(isValid),
                Long.toString(sourceFile.length()),
                Long.toString(sourceFile.lastModified())
        });
        cursor.moveToFirst();
        return cursor;
    }

    public boolean isValid() {
        return mIsValid;
    }

    public long getSize() {
        return mSize;
    }

    public long getModified() {
        return mModified;
    }
}
