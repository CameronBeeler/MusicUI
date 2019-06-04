package sample.Model;

public
class CONSTANTS
{
    public static final int ORDER_BY_NONE=1;
    public static final int ORDER_BY_ASC=2;
    public static final int ORDER_BY_DESC=3;
    public static final String TABLE_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/cam/DEV/JAVA/Music/" + TABLE_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONGS_ID = "_id";
    public static final String COLUMN_SONGS_TRACK = "track";
    public static final String COLUMN_SONGS_TITLE = "title";
    public static final String COLUMN_SONGS_ALBUM_ID = "album";
    public static final int INDEX_SONGS_ID = 1;
    public static final int INDEX_SONGS_TRACK=2;
    public static final int INDEX_SONGS_TITLE = 3;
    public static final int INDEX_SONGS_ALBUM_ID = 4;

    public static final String QUERY_ARTISTS_START =
            "SELECT * FROM " + TABLE_ARTISTS;
    public static final String QUERY_ARTISTS_BY_ARTIST_SORT =
            " ORDER BY " + COLUMN_ARTIST_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    // GIVE me the song and return the artist, the song, the album, and the track that the song has
    public static final String QUERY_SONG_ARTIST_ALBUM_TRACK_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_TITLE + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_TRACK + " FROM " +
            TABLE_SONGS + " INNER JOIN " +  TABLE_ALBUMS + " ON " +
            TABLE_SONGS + "." + COLUMN_SONGS_ALBUM_ID + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " +  TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " WHERE " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
    " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

//  CREATE VIEW IF NOT EXISTS artist_list
//  AS SELECT artists.name, albums.name
//  AS album, songs.track, songs.title
//  FROM songs
//  INNER JOIN albums
//  ON songs.album = albums._id
//  INNER JOIN artists
//  ON albums.artist = artists._id
//  ORDER BY artists.name, albums.name, songs.track

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW =
    "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW +
    " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." +
            COLUMN_ALBUM_NAME +
    " AS " + TABLE_SONGS + "." + COLUMN_SONGS_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE +
    " FROM " + TABLE_SONGS +
    " INNER JOIN " + TABLE_ALBUMS +
    " ON " + TABLE_SONGS + "." + COLUMN_SONGS_ALBUM_ID + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
    " INNER JOIN " + TABLE_ARTISTS +
    " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
    " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." +
            COLUMN_ALBUM_NAME + ", " +  TABLE_SONGS + "." + COLUMN_SONGS_TRACK;

    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTIST_NAME + ", " +
           COLUMN_SONGS_ALBUM_ID + ", " + COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
           " WHERE " + COLUMN_SONGS_TITLE + " = \"";

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTIST_NAME + ", " +
           COLUMN_SONGS_ALBUM_ID + ", " + COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
           " WHERE " + COLUMN_SONGS_TITLE + " = ?";

    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
           '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID +
                  " FROM "  + TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS +
           '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?,?)";

    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID +
           " FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?" +
           " AND " + COLUMN_ALBUM_ARTIST + " = ?";

    public static final String INSERT_SONG = "INSERT INTO " + TABLE_SONGS +
           '(' + COLUMN_SONGS_TRACK + ", " + COLUMN_SONGS_TITLE + ", " +
           COLUMN_SONGS_ALBUM_ID + ") VALUES(?, ?, ?)";


    public static final String QUERY_ALBUMS_BY_ARTIST_ID = "SELECT * FROM " + TABLE_ALBUMS +
           " WHERE " + COLUMN_ALBUM_ARTIST + " = ? ORDER BY " + COLUMN_ALBUM_NAME + " COLLATE NOCASE";

    public static final String UPDATE_ARTIST_NAME = "UPDATE " + TABLE_ARTISTS +
           " SET " + COLUMN_ARTIST_NAME + " = ? WHERE " + COLUMN_ARTIST_ID + " = ?"; }
