package net.adamhilton.handylyst.ui.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.EditText;

import net.adamhilton.handylyst.R;
import net.adamhilton.handylyst.data.model.List;
import net.adamhilton.handylyst.data.model.RealmString;
import net.adamhilton.handylyst.ui.base.BaseActivity;
import net.adamhilton.handylyst.ui.edit.recyclerview.ListItemAdapter;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends BaseActivity
        implements EditScreenContract.View, ListItemAdapter.ButtonClickEventListener {

    public static final String EXTRA_LIST = "net.adamhilton.handylyst.LIST";
    public static final String EXTRA_IS_NEW_LIST = "net.adamhilton.handylyst.ISNEWLIST";

    private List list = new List();

    private ListItemAdapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditPresenter presenter =  new EditPresenter(this);

    private boolean isNewList;

    @BindView(R.id.edit_name_text)
    EditText name_text;
    @BindView(R.id.list_item_recycler_view)
    RecyclerView list_item_recycler_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isNewList = bundle.getBoolean(EXTRA_IS_NEW_LIST);

            if(bundle.containsKey(EXTRA_LIST)) {
                list = Parcels.unwrap(bundle.getParcelable(EXTRA_LIST));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        listAdapter.unsubscribeEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeView();
        listAdapter.subscribeEventListener(this);
    }

    private void initializeView() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        name_text.setText(list.Name);

        layoutManager = new LinearLayoutManager(this);
        list_item_recycler_view.setLayoutManager(layoutManager);

        listAdapter = new ListItemAdapter(list);
        list_item_recycler_view.setAdapter(listAdapter);
    }

    @OnClick(R.id.save)
    public void onSaveClicked() {
        String name = String.valueOf(name_text.getText());
        this.list.Name = name;
        if(isNewList) {
            presenter.createList(list);
        } else {
            presenter.updateList(list);
        }
    }

    @OnClick(R.id.add_list_item_button)
    public void onListItemAddClicked() {
        presenter.addListItem();
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void addItemToList(String item) {
        RealmString realmString = new RealmString();
        realmString.value = item;
        this.list.Items.add(realmString);
        listAdapter.notifyItemInserted(this.list.Items.size() - 1);
    }

    @Override
    public void deleteButtonClicked(int position) {
        presenter.deleteItem(list, position);
    }

    @Override
    public void removeListItem(int position) {
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, list.Items.size());
    }
}
