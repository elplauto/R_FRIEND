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

public class DeezerDataSearcher {

    CreationRecommandationActivity context;
    DeezerConnect deezerConnect;

    AlbumPlayer albumPlayer;
    TrackPlayer trackPlayer;

    public DeezerDataSearcher(CreationRecommandationActivity context, String appId) {
        this.context = context;
        deezerConnect = new DeezerConnect(context, appId);
        String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY };
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
    }

    public void jouerAlbum(int albumId) {
        albumPlayer.playAlbum(albumId);
    }


    public void jouerMorceau(int trackId) {
        trackPlayer.playTrack(trackId);
    }

    public void rechercheMorceau(String nom) {

        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                final List<Morceau> morceaux = new ArrayList<>();
                List<Track> tracks = (List<Track>) result;
                for (Track track : tracks) {
                    morceaux.add(new Morceau(track.getId()+"",track.getTitle(),track.getDuration(), track.getArtist().getName(), track.getAlbum().getTitle(), track.getAlbum().getBigImageUrl()));
                }
                context.rechercheMorceauReponse(morceaux);
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
                    listAlbums.add(new com.pts3.r_friend.Album(album.getId()+"",album.getTitle(),album.getArtist().getName(),album.getNbTracks(), album.getBigImageUrl()));
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
                    artistes.add(new Artiste(artist.getId()+"",artist.getName(), artist.getNbAlbums(), artist.getBigImageUrl(),null));
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

    public void rechercherTitreArtiste(final Artiste artiste) {
        RequestListener listener = new JsonRequestListener() {

            public void onResult(Object result, Object requestId) {
                List<Track> tracks = (List<Track>) result;
                for (Track track : tracks) {
                   if (track.getArtist().getId() == Long.parseLong(artiste.getId())) {
                       context.rechercheTitreArtisteReponse(track.getId(),artiste);
                       return;
                   }
                }
                context.rechercheTitreArtisteReponse(null,artiste);
            }

            public void onUnparsedResult(String requestResponse, Object requestId) {}

            public void onException(Exception e, Object requestId) {}
        };

        DeezerRequest request = DeezerRequestFactory.requestSearchTracks(artiste.getNom());

        // set a requestId, that will be passed on the listener's callback methods
        request.setId("trackRequest");

        // launch the request asynchronously
        deezerConnect.requestAsync(request, listener);
    }

    public void debug(String s) {
        Log.e("-----",s);
    }
}
