package by.app.packagemanagertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class WordListAdapter extends
        RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final List<AppInfo> mWordList;
    private final LinkedList<String> hashList;
    private final LayoutInflater mInflater;

    class WordViewHolder extends RecyclerView.ViewHolder
            {
        public final TextView wordItemView;
        public final TextView hashItemView;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.text);
            hashItemView = itemView.findViewById(R.id.hash);
            this.mAdapter = adapter;
        }
    }

    public WordListAdapter(Context context, List<AppInfo> wordList, LinkedList<String> appHash) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
        this.hashList = appHash;
    }

    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(
                R.layout.applist_item, parent, false);
        return new WordViewHolder(mItemView, this);
    }


    @Override
    public void onBindViewHolder(WordListAdapter.WordViewHolder holder, int position) {
        // Retrieve the data for that position.
        AppInfo mCurrent = mWordList.get(position);
        String hashCurrent = hashList.get(position);
        // Add the data to the view holder.
        holder.wordItemView.setText(mCurrent.toString());
        holder.hashItemView.setText(hashCurrent);
    }


    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}
