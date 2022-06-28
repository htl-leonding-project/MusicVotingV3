import { Artist } from "./artist.model"

export interface Song {
  // private Long id;


  //   private String songName;
  //   private String videoUrl;
  //   private String thumbnail;

  //   private String songId;

  //   private LocalDateTime timeAdded;


  songName: string
  videoUrl: string
  songId: string
  thumbnail: string
  duration: number
  artist: Artist
}
