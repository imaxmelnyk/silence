# Silence: divide an audio stream by silence

## Background

Business-to-business digital content distribution is a mish-mash of various standards and
practices which haven't always been subjects to the best planning. At Beat, we tame this
complexity. But for the purposes of this task, we can afford ourselves the luxury of making
a few simplifications. As such, the following is hypothetical and not an accurate or
complete description of how things work.

An audio book is delivered to us as one long audio file. We would like to present our audio
books as series of chapters, where the user can select any chapter to start listening to it.
But the content owner does not provide us with a list of chapters, and no information about
where each chapter begins or ends. Fortunately for us, it turns out that the narrator always takes a
rather long break between chapters, typically around three seconds. Our strategy is therefore
to distribute a descriptor along with the audio streams so that clients can render the chapter
list and know what the offset of each chapter is. This way, when the user selects a chapter,
the player can seek into the stream to the correct position.

We then realise that some books have very long chapters, and we would like to provide
finer granularity in these cases. By allowing the user to skip between meaningful parts
inside each chapter, we may better approach the experience of flipping through the pages of
a paper book. Here we can divide by shorter narration breaks, such as one second.

So we want to divide books into a sequence of _segments_, where some segments are complete
short chapters, and some are parts of long chapters.

We have at our disposal a tool which detects silence intervals longer than 500 milliseconds
in audio files. After analysing a file, it produces XML like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<silences>
  <silence from="PT3M9S" until="PT3M11S" />
  <silence from="PT15M22S" until="PT15M25S" />
  <silence from="PT28M23S" until="PT28M26.4S" />
</silences>
```

The time offsets are ISO 8601 duration strings as described on
https://en.wikipedia.org/wiki/ISO_8601#Durations and as supported by `java.time.Duration`
and similar libraries for other environments.

In the example, there is silence which starts at 3 minutes and 9 seconds into the file, and
lasts until 3 minutes and 11 seconds – in other words, two seconds of silence. The silence
intervals are always given in chronological order.
