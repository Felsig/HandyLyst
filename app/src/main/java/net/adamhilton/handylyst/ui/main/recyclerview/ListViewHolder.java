package net.adamhilton.handylyst.ui.main.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.adamhilton.handylyst.HandyLystApp;
import net.adamhilton.handylyst.R;
import net.adamhilton.handylyst.ui.edit.EditActivity;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListViewHolder extends RecyclerView.ViewHolder{

    private final Context context;

    @BindView(R.id.name)
    TextView name;

    public ListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    @OnClick
    public void onItemClicked() {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(EditActivity.EXTRA_LIST, Parcels.wrap(HandyLystApp.getListRepo().getAll().get(getAdapterPosition())));
        context.startActivity(intent);
    }
}
