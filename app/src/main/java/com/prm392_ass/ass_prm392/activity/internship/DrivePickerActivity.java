package com.prm392_ass.ass_prm392.activity.internship;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DrivePickerActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FILE_PICKER = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openDriveFilePicker();
    }

    private void openDriveFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Nếu bạn muốn hỗ trợ chọn nhiều file, bật dòng dưới
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_CODE_FILE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILE_PICKER) {
            if (resultCode == RESULT_OK && data != null) {
                Uri fileUri = data.getData();
                // Trả URI về caller (InternshipDetailActivity)
                Intent result = new Intent();
                result.setData(fileUri);
                setResult(RESULT_OK, result);
            } else {
                // Hủy hoặc lỗi
                Toast.makeText(this, "Không chọn được file!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
            }
        }

        finish();
    }
}
