package com.tsl.baseapp.model.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsl.baseapp.R;
import com.tsl.baseapp.model.objects.Player;

import java.util.List;

/**
 * Created by Kevin on 9/17/15.
 */
public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.Holder> {

    public static String TAG = PlayersAdapter.class.getSimpleName();

    private List<Player> mPlayers;

    public PlayersAdapter(List<Player> players) {
        mPlayers = players;
    }

    public void addPlayer(Player player) {
        mPlayers.add(player);
        notifyDataSetChanged();
    }

    @Override
    public PlayersAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(PlayersAdapter.Holder holder, int position) {

        Player currentPlayer = mPlayers.get(position);
        holder.mName.setText(currentPlayer.mName);
        holder.mPosition.setText(currentPlayer.mPosition);
        holder.mTeam.setText(currentPlayer.mTeam);
        holder.mStandard.setText("Standard: " + currentPlayer.mStandardScoring);
        holder.mPpr.setText("PPR: " + currentPlayer.mPprScoring);

    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public TextView mName, mPosition, mTeam, mStandard, mPpr;

        public Holder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.playerName);
            mPosition = (TextView) itemView.findViewById(R.id.playerPosition);
            mTeam = (TextView) itemView.findViewById(R.id.playerTeam);
            mStandard = (TextView) itemView.findViewById(R.id.standardScore);
            mPpr = (TextView) itemView.findViewById(R.id.pprScore);
        }
    }
}
