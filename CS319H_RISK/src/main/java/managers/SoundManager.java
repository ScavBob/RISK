package managers;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundManager {

    private String musicFilePath;
    private Media musicFile;
    private MediaPlayer background_theme;

    private String alternativePath;
    private Media alternative_music;
    private MediaPlayer alternative_background_theme;

    private String congratsPath;
    private Media congrats;
    private MediaPlayer congratsSound;

    private String clickPath;
    private Media click;
    private MediaPlayer clickSound;

    public SoundManager()
    {
        musicFilePath = getClass().getResource("/musics/medal-of-honor-european-assault-soundtrack-main-theme-hq.mp3").toExternalForm();
        musicFile = new Media(musicFilePath);
        background_theme = new MediaPlayer(musicFile);

        alternativePath = getClass().getResource("/musics/battlefield-1942-theme-song.mp3").toExternalForm();
        alternative_music = new Media(alternativePath);
        alternative_background_theme = new MediaPlayer(alternative_music);

        congratsPath = getClass().getResource("/musics/you-win-street-fighter-sound-effect.mp3").toExternalForm();
        congrats = new Media(congratsPath);
        congratsSound = new MediaPlayer(congrats);

        clickPath = getClass().getResource("/musics/mouse-click-sound-effect-hd.mp3").toExternalForm();
        click = new Media(clickPath);
        clickSound = new MediaPlayer(click);

        setInitialVolume();
    }

    public void mute() {
        background_theme.setVolume(0);
        congratsSound.setVolume(0);
        clickSound.setVolume(0);
    }

    public void setInitialVolume() {
        updateSoundVolumeInitialPosition();
    }

    public void updateSoundVolumeInitialPosition() {
        background_theme.setVolume(0.05);
        congratsSound.setVolume(1);
        clickSound.setVolume(1);
    }

    public void startPlayMusic() {
        background_theme.setCycleCount(Integer.MAX_VALUE);
        background_theme.play();
    }
    public void playCongrats() {
        congratsSound.seek(Duration.ZERO);
        congratsSound.play();
    }


    public void playPrimary() {
        background_theme.seek(Duration.ZERO);
        background_theme.play();
    }

    public void playClick() {
        clickSound.seek(Duration.ZERO);
        clickSound.play();
    }
}