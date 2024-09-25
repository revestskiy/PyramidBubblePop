package com.skyflygame.pyramidbubblepop

import android.content.Context
import android.media.MediaPlayer

object SoundManager {
    private lateinit var musicPlayer: MediaPlayer
    private lateinit var soundPlayer: MediaPlayer
    private lateinit var soundPopPlayer: MediaPlayer

    fun init(context: Context) = runCatching {
        musicPlayer = MediaPlayer.create(context, R.raw.music)
        soundPlayer = MediaPlayer.create(context, R.raw.sound)
        soundPopPlayer = MediaPlayer.create(context, R.raw.pop)
        setMusicVolume()
        setSoundVolume()
    }

    fun pauseMusic() = runCatching {
        musicPlayer.pause()
        soundPlayer.pause()
        soundPopPlayer.pause()
    }

    fun resumeMusic() = runCatching {
        if (!musicPlayer.isPlaying && Prefs.musicVolume > 0) {
            setMusicVolume()
            musicPlayer.start()
            musicPlayer.isLooping = true
        }
    }

    fun onDestroy() = runCatching {
        musicPlayer.release()
        soundPlayer.release()
        soundPopPlayer.release()
    }


    fun playSound() = runCatching {
        soundPlayer.setVolume(Prefs.soundVolume, Prefs.soundVolume)
        soundPlayer.start()
        soundPlayer.isLooping = false
    }

    fun playSoundPop() = runCatching {
        soundPopPlayer.setVolume(Prefs.soundVolume, Prefs.soundVolume)

        // Если звук уже играет, останавливаем его
        if (soundPopPlayer.isPlaying) {
            soundPopPlayer.stop() // Полностью останавливаем плеер
            soundPopPlayer.prepare() // Готовим его к воспроизведению нового звука
        }

        soundPopPlayer.start() // Начинаем воспроизведение нового звука
        soundPopPlayer.isLooping = false
    }


    fun setSoundVolume() {
        soundPopPlayer.setVolume(Prefs.soundVolume, Prefs.soundVolume)
        soundPlayer.setVolume(Prefs.soundVolume, Prefs.soundVolume)
    }

    fun setMusicVolume() = runCatching {
        if (Prefs.musicVolume == 0f) {
            musicPlayer.stop()
            musicPlayer.prepare()
        }
        else {
            musicPlayer.start()
            musicPlayer.isLooping = true
        }
        musicPlayer.setVolume(Prefs.musicVolume, Prefs.musicVolume)
    }
}