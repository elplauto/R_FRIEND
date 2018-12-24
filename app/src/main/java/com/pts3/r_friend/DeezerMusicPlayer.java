package com.pts3.r_friend;

import android.util.Log;

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
                Log.e("----",playerState.toString());
            }
        });
    }

    public void jouerAlbum(int albumId) {
        albumPlayer.playAlbum(albumId);
    }


    public void jouerMorceau(int trackId) {
        trackPlayer.playTrack(trackId);
    }
}
