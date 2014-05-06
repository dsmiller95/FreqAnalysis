FreqAnalysis
============


This code will interface with an arduino and a magnetic pickup to achieve correct actuation.

The magnetic pickup will be hooked up to the microphone jack, and this code will listen for
changes in the frequency of the input. The changes in frequency will indicate changes in the
tension of the string being picked up. When this is detected, the arduino will be alerted
to send a signal to a piston to extract the heavy object causing the change in pitch.
Repo for audio analysis and serial communication code
