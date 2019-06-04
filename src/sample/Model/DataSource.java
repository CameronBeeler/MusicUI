package sample.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static sample.Model.CONSTANTS.*;

public
class DataSource
{

    private Connection conn;
    private PreparedStatement querySongInView;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;
    private PreparedStatement queryAlbum;
    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbumsByArtistID;
    private PreparedStatement updateArtistName;

    private static DataSource instance = new DataSource();

    private DataSource()
    {
    }

    public static DataSource getInstance()
    {
//        if (instance == null) {instance = new DataSource();}  // Lazy Instantiation, but not thread safe
        return instance;
        // DataSource.getInstance().methodName()...
    }
    public boolean open()
    {
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONG);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbumsByArtistID = conn.prepareStatement(QUERY_ALBUMS_BY_ARTIST_ID);
            updateArtistName = conn.prepareStatement(UPDATE_ARTIST_NAME);
            return true;
        }catch(SQLException e)
        {
            System.out.println("Couldn't connect to database" + e.getMessage());
            return false;
        }

    }

    public void close()
    {
        try
        {
            if (querySongInView != null)
                querySongInView.close();

            if (insertIntoArtists != null)
                insertIntoArtists.close();

            if(insertIntoAlbums != null)
                insertIntoAlbums.close();

            if(insertIntoSongs != null)
                insertIntoSongs.close();

            if(queryAlbum != null)
                queryAlbum.close();

            if(queryArtist != null)
                queryArtist.close();

            if(queryAlbumsByArtistID != null)
                queryAlbumsByArtistID.close();

            if(updateArtistName != null)
                updateArtistName.close();

            if(conn!=null)
                conn.close();

        }catch(SQLException e){
            System.out.println("Connection failed to close " + e.getMessage());
        }

    }

    public List<Album> queryAlbumForArtistID(int id)
    {
       try
       {
           queryAlbumsByArtistID.setInt(1, id);
           ResultSet results = queryAlbumsByArtistID.executeQuery();
           List<Album> albums = new ArrayList<>();
           while(results.next())
           {
               Album album = new Album();
               album.setId(results.getInt(1));
               album.setName(results.getString(2));
               album.setArtist_id(id);
               albums.add(album);
           }

           return albums;
       }
       catch (SQLException s)
       {
           System.out.println("Query failed: " + s.getMessage());
           return null;
       }
    }

    public
    List<Artist> queryArtists(int sortOrder)
    {
        StringBuilder sb = new StringBuilder(QUERY_ARTISTS_START);
        if(sortOrder != ORDER_BY_NONE)
        {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_ASC)
            { sb.append(" ASC ");}
            else
            {sb.append(" DESC ");}
        }

        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery( sb.toString() ))
        {

            List<Artist> artists = new ArrayList<>();
            while (results.next())
            {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }

            return artists;

        }catch (SQLException e)
        {
            System.out.println("#3 Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Album> queryAlbums()
    {
        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM albums");)
        {
            List<Album> albums = new ArrayList<>();
            while (results.next())
            {
                Album album = new Album();
                album.setId(results.getInt(INDEX_ALBUM_ID));
                album.setName(results.getString(INDEX_ALBUM_NAME));
                album.setArtist_id(results.getInt(INDEX_ALBUM_ARTIST));
                albums.add(album);
            }

            return albums;

        }
        catch (SQLException e)
        {
            System.out.println("#4 Query failed: " + e.getMessage());
            return null;
        }
    }


    public static
    String padLeft(String s, int n)
    {
//        int    length = n - s.length() ;
        String x      = String.format("%" + n + "s", s);
        return x;
    }


    public static
    String padRight(String s, int n)
    {
//        int    length = n - s.length() ;
        String x      = String.format("%-" + n + "s", s);
        return x;
    }

    public List<String> queryAlbumsForArtist(String artistName, int sortOrder)
    {
        List<String> albums = new ArrayList<>();

        StringBuilder sb = new StringBuilder (QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");
        if (sortOrder != ORDER_BY_NONE)
        {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if(sortOrder==ORDER_BY_DESC){
                sb.append(" DESC ");
            }
            else {
                sb.append(" ASC ");
            }
        }

        System.out.println("SQL statement = " + sb.toString());

        try(Statement stmt = conn.createStatement();
        ResultSet sqlResults = stmt.executeQuery(sb.toString());)
        {
            while(sqlResults.next())
            {

                albums.add(sqlResults.getString(1));
            }
            for(int i=0;i<albums.size();i++)
            {
                System.out.println(albums.get(i).toString());
            }

        }catch(SQLException e)
        {
            System.out.println("Failure in queryAlbumsForArtist " + e.getMessage());
            return null;
        }
        return albums;
    }


    public void querySongsMetaData()
    {
        String sql = "SELECT * FROM " + TABLE_SONGS;
        try(Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(sql))
        {
            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
            for (int i=1;i<=numColumns;i++)
            {
                System.out.format("Columns %d in the songs table names %s\n",
                                   i, meta.getColumnName(i));
            }
        }catch(SQLException e)
        {
            System.out.println("querySongsMetaData " + e.getMessage() );
        }
    }

    public int getCount(String table)
    {
        String sql = "SELECT COUNT(*) AS count FROM " + table ;
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql))
        {
            int count = results.getInt("count");
//            int count = results.getInt(1); // if the column is not named, then use the return ref
            System.out.format("Count = %d\n", count);
            return count;
        } catch(SQLException e)
        {
            System.out.println("#6 Query failed " + e.getMessage());
            return -1;
        }
    }

    public boolean createViewForSongArtist()
    {
        try(Statement statement = conn.createStatement())
        {
            statement.executeQuery(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        } catch(SQLException e)
        {
            System.out.println("#7 Query failed " + e.getMessage());
            return false;
        }
    }

    private int insertArtist(String artistName) throws SQLException
    {
        queryArtist.setString(1, artistName);
        ResultSet results = queryArtist.executeQuery();
        if(results.next())
            return results.getInt(1);
        else
        {
            insertIntoArtists.setString(1, artistName);
            int affectedRows = insertIntoArtists.executeUpdate();
            if (affectedRows != 1)
                throw new SQLException("Could not insert an Artist record");
            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if (generatedKeys.next())
                return generatedKeys.getInt(1);
            else
                throw new SQLException("Could not acquire _id for artist");
        }
    }

    private int insertAlbum(int artistID, String album) throws SQLException
    {
        System.out.println("In the insertAlbum method");
        queryAlbum.setString(1,album); // the 1st param is a placeholder position...
        queryAlbum.setInt(2, artistID); // the 2nd param is a placeholder position...
        ResultSet results = queryAlbum.executeQuery();
        if(results.next())
        {
            System.out.println("found the album and returning");
            return results.getInt(1);
        }
        else
        {
            System.out.println("Did not find the album, creating...");
            insertIntoAlbums.setString(1, album);
            insertIntoAlbums.setInt(2  , artistID);
            System.out.println("Album name " + album + "\nArtist ID " + artistID);
            int affectedRows = insertIntoAlbums.executeUpdate();
            if(affectedRows != 1)
            {
                System.out.println("Did not successfully create the album.  affected Rows = " + affectedRows);
                throw new SQLException("Could not create a new album");
            }
            else
            {
                ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
                if(generatedKeys.next())
                {
                    System.out.println("There were generated keys - " + generatedKeys);
                    return generatedKeys.getInt(1);
                }
                else
                {
                    System.out.println("There were NO generated keys");
                    throw new SQLException("Could not acquire the album _id");
                }
            }
        }
    }

    public boolean updateArtistName(int id, String newName)
    {
        try
        {
            updateArtistName.setString(1, newName);
            updateArtistName.setInt(2, id);
            int affectedRecords = updateArtistName.executeUpdate();
            return affectedRecords == 1;

        }catch (SQLException e)
        {
            System.out.println("Update Failed: " + e.getMessage());
            return false;
        }
    }


    public boolean AddNewSongTransaction(String songName, String artistName, String albumName, int trackNbr)
    {
/* artist name, Album name, Song track#, Song title, Album ID */
        try
        {
            conn.setAutoCommit(false);
            int artistID = insertArtist(artistName);
            if (artistID != 0)
            {
                System.out.println("Successfully found or added artist " + artistName);
            }
            int albumID = insertAlbum(artistID, albumName);
            System.out.println("albumID = " + albumID);
            if (albumID != 0)
            {
                System.out.println("Successfully found or added album " + albumName);
            }

            insertIntoSongs.setInt(1, trackNbr);
            insertIntoSongs.setString(2, songName);
            insertIntoSongs.setInt(3, albumID);
            int affectedRows = insertIntoSongs.executeUpdate();

            if(affectedRows == 1)
                conn.commit();
            else
                throw new SQLException("Failed to insert the song");
        }
        catch(Exception e)
        {
            System.out.println("Failed to enter the song" + e.getMessage());
            try
            {
                System.out.println("Performing rollback");
                conn.rollback();
                return false;
            }
            catch(SQLException e2)
            {
                System.out.println(e2.getMessage() + " -> failed to execute rollback");
            }
        }
        finally
        {
            System.out.println("In the finally loop");
            try
            {
                conn.setAutoCommit(true);
                return true;
            }catch (SQLException e3)
            {
                System.out.println(" Failed to restore autocommit " + e3.getMessage());
            }
          return false;
        }
    }

}
