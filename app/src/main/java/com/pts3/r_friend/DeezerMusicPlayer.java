package com.pts3.r_friend;

import android.util.Log;
import android.widget.ImageButton;

import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.event.OnPlayerStateChangeListener;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

public class DeezerMusicPlayer {

    MainActivity context;
    DeezerConnect deezerConnect;
    AlbumPlayer albumPlayer;
    TrackPlayer trackPlayer;
    int idAlbumEnCours;
    int idMorceauEnCours;
    ImageButton imageButton;

    public DeezerMusicPlayer(MainActivity context, String appId) {
        this.context = context;
        deezerConnect = new DeezerConnect(context, appId);
        String[] permissions = new String[]{
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY};
        try {
            albumPlayer = new AlbumPlayer(context.getApplication(), deezerConnect, new WifiAndMobileNetworkStateChecker());
        } catch (DeezerError | TooManyPlayersExceptions deezerError) {
            deezerError.printStackTrace();
        }
        try {
            trackPlayer = new TrackPlayer(context.getApplication(), deezerConnect, new WifiAndMobileNetworkStateChecker());
        } catch (DeezerError | TooManyPlayersExceptions deezerError) {
            deezerError.printStackTrace();
        }

        albumPlayer.addOnPlayerStateChangeListener(new OnPlayerStateChangeListener() {
            @Override
            public void onPlayerStateChange(PlayerState playerState, long l) {
                if (playerState.toString().equals("PLAYBACK_COMPLETED")) {
                    idAlbumEnCours=0;
                }
            }
        });

        trackPlayer.addOnPlayerStateChangeListener(new OnPlayerStateChangeListener() {
            @Override
            public void onPlayerStateChange(PlayerState playerState, long l) {
                if (playerState.toString().equals("PLAYBACK_COMPLETED")){
                    idMorceauEnCours=0;
                    imageButton.setBackgroundResource(R.drawable.play);
                }
            }
        });


        idAlbumEnCours=0;
        idMorceauEnCours=0;
    }

    public void jouerAlbum(int albumId, ImageButton imageButton) {
        if (this.imageButton != null) {
            this.imageButton.setBackgroundResource(R.drawable.play);
        }
        idAlbumEnCours=albumId;
        stopMorceau();
        albumPlayer.playAlbum(albumId);
        this.imageButton=imageButton;
    }

    public void jouerMorceau(int trackId, ImageButton imageButton) {
        if (this.imageButton != null) {
            this.imageButton.setBackgroundResource(R.drawable.play);
        }
        idMorceauEnCours=trackId;
        stopAlbum();
        trackPlayer.playTrack(trackId);
        this.imageButton=imageButton;
    }

    public void stopMorceau() {
        trackPlayer.stop();
        idMorceauEnCours=0;
    }

    public void stopAlbum() {
        albumPlayer.stop();
        idAlbumEnCours=0;
    }

    public String getAlbumPlayerState() {
        return albumPlayer.getPlayerState().toString();
    }

    public String getTrackPlayerState() {
        return trackPlayer.getPlayerState().toString();
    }

    public int getIdAlbumEnCours() {
        return idAlbumEnCours;
    }

    public int getIdMorceauEnCours() {
        return idMorceauEnCours;
    }
}
