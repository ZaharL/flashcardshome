package com.learningstarz.myflashcards.ui.components.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.data_storage.DataManager;
import com.learningstarz.myflashcards.interfaces.DataTransferable;
import com.learningstarz.myflashcards.tools.DownloadImageTask;
import com.learningstarz.myflashcards.types.Card;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.activities.MyDecksActivity;
import com.learningstarz.myflashcards.ui.async_tasks.DeleteCardAsyncTask;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Created by ZahARin on 09.02.2016.
 */
public class EditCardsAdapter extends RecyclerSwipeAdapter<EditCardsAdapter.SimpleViewHolder> {

    Context context;
    List<Card> cards;
    Deck deck;
    private SwipeLayout prevLayout = null; // previous layout. needs to close it when new opens
    DataTransferable dtInterface;

    public EditCardsAdapter(Context context, Deck deck, DataTransferable dtInterface) {
        this.context = context;
        this.cards = deck.getCards();
        this.deck = deck;
        this.dtInterface = dtInterface;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_edit, parent, false);
        return new SimpleViewHolder(v);
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView tvFace;
        TextView tvBack;
        ImageView ivFace;
        ImageView ivBack;
        ImageButton btnDelete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.CardsActivity_swipeLayout);
            tvFace = (TextView) itemView.findViewById(R.id.CardsActivity_etCardFace);
            tvBack = (TextView) itemView.findViewById(R.id.CardsActivity_etCardBack);
            ivFace = (ImageView) itemView.findViewById(R.id.CardsActivity_ivFace);
            ivBack = (ImageView) itemView.findViewById(R.id.CardsActivity_ivBack);
            btnDelete = (ImageButton) itemView.findViewById(R.id.CardsActivity_btnDeleteCard);
        }
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final Card card = cards.get(position);

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO create transition to edit card
            }
        });

        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {

            @Override
            public void onStartOpen(SwipeLayout layout) {
                super.onStartOpen(layout);
                if (prevLayout != null && prevLayout != layout) {
                    prevLayout.close();
                    prevLayout = layout;
                } else {
                    prevLayout = layout;
                }
                layout.open();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                super.onStartClose(layout);

                layout.close();
            }
        });


        String quest = context. getString(R.string.face) + card.getQuestion().replaceAll("<p>", "").replaceAll("</p>", "").replaceAll(" ", "");
        String answ = context. getString(R.string.back) + card.getQuestion().replaceAll("<p>", "").replaceAll("</p>", "").replaceAll(" ", "");

        viewHolder.tvFace.setText(Html.fromHtml(quest));
        viewHolder.tvBack.setText(Html.fromHtml(answ));
        Formatter fm = new Formatter();
        if (!card.getImage1().equals("")) {
            String imageFacePath = card.getImagePath() + card.getImage1();
            fm.format(context.getString(R.string.url_host, imageFacePath));
            new DownloadImageTask(viewHolder.ivFace).execute(fm.toString());
        }
        if (!card.getImage2().equals("")) {
            String imageBackPath = card.getImagePath() + card.getImage2();
            fm.format(context.getString(R.string.url_host, imageBackPath));
            new DownloadImageTask(viewHolder.ivBack).execute(fm.toString());
        }
        fm.close();

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setTitle(R.string.CardsActivity_Dialog_DeleteCard_Title);
                deleteDialog.setMessage(R.string.CardsActivity_Dialog_DeleteCard_Message);
                deleteDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                        cards.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cards.size());
                        closeAllItems();

                        DataManager.deleteCardByUId(deck, card);

                        //transfer data to activity
                        dtInterface.onSetIntData(cards.size());

                        Formatter fm = new Formatter();
                        fm.format(context.getString(R.string.url_delete_deck_card), DataManager.getUserToken(), card.getUid());
                        new DeleteCardAsyncTask(context).execute(fm.toString());
                        fm.close();
                    }
                });
                deleteDialog.setNegativeButton(R.string.no, null);
                deleteDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.CardsActivity_swipeLayout;
    }


}
