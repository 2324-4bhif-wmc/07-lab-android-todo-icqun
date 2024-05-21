package at.htl.todo;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import at.htl.todo.model.TodoService;
import at.htl.todo.ui.layout.DetailsView;
import at.htl.todo.util.Config;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsActivity extends ComponentActivity {
    static final String TAG = TodoApplication.class.getSimpleName();

    @Inject
    DetailsView detailsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Log.e(TAG, "Extra data for Details activity is missing!");
            return;
        }

        if(!extras.containsKey("idx")) {
            Log.e(TAG, "Index needed for Details activity is missing!");
            return;
        }

        detailsView.buildContent(this, extras.getInt("idx"));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
