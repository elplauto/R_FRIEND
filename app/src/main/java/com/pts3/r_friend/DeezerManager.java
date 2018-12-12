package com.pts3.r_friend;

import android.util.Log;

import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Artist;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

import java.util.ArrayList;
import java.util.List;

public class DeezerManager {

    CreationRecommandationActivity context;
    DeezerConnect deezerConnect;

    public DeezerManager(CreationRecommandationActivity context, String appId) {
        this.context = context;
        deezerConnect = new DeezerConnect(context, appId);
        String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY };
    }

    public void jouerAlbum(Album album) {
        AlbumPlayer albumPlayer;
        try {
            albumPlayer = new AlbumPlayer(context.getApplication(), deezerConnect, new WifiAndMobileNetworkStateChecker());
            albumPlayer.playAlbum(album.getId());
        } catch (TooManyPlayersExceptions | DeezerError tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        }
    }

    public void jouerMusique(Track track) {
        TrackPlayer trackPlayer;
        try {
            trackPlayer = new TrackPlayer(context.getApplication(), deezerConnect, new WifiAndMobileNetworkStateChecker());
            trackPlayer.playTrack(track.getId());
        } catch (TooManyPlayersExceptions | DeezerError tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        }
    }

    public void rechercheMusique(String nom) {

        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                final List<Musique> musiques = new ArrayList<>();
                List<Track> tracks = (List<Track>) result;
                for (Track track : tracks) {
                   musiques.add(new Musique(track.getId()+"",track.getTitle(),track.getDuration()+"s", track.getArtist().getName()));
                }
                context.rechercheMusiqueReponse(musiques);
            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };

        DeezerRequest request = DeezerRequestFactory.requestSearchTracks(nom);

        // set a requestId, that will be passed on the listener's callback methods
        request.setId("trackRequest");

        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);
    }

    public void rechercheAlbum(String nom) {

        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                final List<com.pts3.r_friend.Album> listAlbums = new ArrayList<>();
                List<Album> albums = (List<Album>) result;
                for (Album album : albums) {
                    listAlbums.add(new com.pts3.r_friend.Album(album.getId()+"",album.getTitle(),album.getArtist().getName(),album.getNbTracks()+""));
                }
                context.rechercheAlbumReponse(listAlbums);
            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };

        DeezerRequest request = DeezerRequestFactory.requestSearchAlbums(nom);

        // set a requestId, that will be passed on the listener's callback methods
        request.setId("trackRequest");

        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);
    }

    public void rechercheArtiste(String nom) {

        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                final List<Artiste> artistes = new ArrayList<>();
                List<Artist> artists = (List<Artist>) result;
                for (Artist artist : artists) {
                    artistes.add(new Artiste(artist.getId()+"",artist.getName()));
                }
                context.rechercheArtisteReponse(artistes);
            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };

        DeezerRequest request = DeezerRequestFactory.requestSearchArtists(nom);

        // set a requestId, that will be passed on the listener's callback methods
        request.setId("trackRequest");

        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);
    }

    public void debug(String s) {
        Log.e("-----",s);
    }
}
