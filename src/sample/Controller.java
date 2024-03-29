package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import sample.Model.Album;
import sample.Model.Artist;
import sample.Model.CONSTANTS;
import sample.Model.DataSource;
import javafx.scene.control.TableView;


public
class Controller
{
    @FXML
    private
    TableView artistTable;

    @FXML
    private
    ProgressBar progressBar;

    @FXML
    public
    void listArtists()
    {
        Task<ObservableList<Artist>> task = new GetAllArtistTask();
        artistTable.itemsProperty().bind(task.valueProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);
        task.setOnSucceeded(e->progressBar.setVisible(false));
        task.setOnFailed(e->progressBar.setVisible(false));

        new Thread(task).start();
    }

    @FXML
    public
    void updateArtistName()
    {
//        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        final Artist artist = (Artist) artistTable.getItems().get(2);
        Task<Boolean> task = new Task<Boolean>()
        {
            @Override
            protected
            Boolean call() throws
                    Exception
            {
                return DataSource.getInstance().updateArtistName(artist.getId(), "AC/DC");
            }
        };
        task.setOnSucceeded(e->
        {
            if (task.valueProperty().get() )
            {
                artist.setName("AC/DC");
                artistTable.refresh();
            }
        });
        new Thread(task).start();
    }

    @FXML
    public
    void listAlbumsForArtist()
    {
        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        if(artist == null)
        {
            System.out.println("NO ARTIST SELECTED");
            return ;
        }

        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>()
        {
            @Override
            protected
            ObservableList<Album> call()
            throws Exception
            {
                return FXCollections.observableArrayList(
                        DataSource.getInstance().queryAlbumForArtistID(artist.getId())
                );
            }
        };
        artistTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }
}

class GetAllArtistTask
        extends Task
{
    @Override
    public
    ObservableList<Artist> call()
    {
        return FXCollections.observableArrayList(DataSource.getInstance().queryArtists(CONSTANTS.ORDER_BY_ASC));
    }
}

