# EnigmaSim

This application was created by someone with a passion for cryptography that can still be grasped by the common
human mind.
  
#####Installation
* Get the ZIP file from `PonaiEnigmaSimPrj/build`
* Extract the ZIP file to a drive you want to run it from
* Make sure you have Java installed (11.0.9.1+1 (AdoptOpenJDK) was used for creating the build, but Java 1.8 or higher is also ok)
* Run `java -jar PonaiEnigmaSim.jar` from the directory you extracted the ZIP file into

#####Altering the software/creating builds
The PonaiEnigmaSim directory is an Eclipse project (I used 32-bit Luna on Windows 8.1) with some custom Project Builders to make it simple.
The latest build was created using IntelliJ IDEA though, so there I manually build the module, ran the setVerion and finally the createBuild
Ant tasks.
Converting this into a Maven project is something I might do sometime, it is quite a trivial task, but at the time I created this project I
was still using Ant =)

#####Credits
All Enigma information necessary to create this application was retrieved from the following sources and I want to
thank them all for helping me out:
  
#####http://www.cryptomuseum.com/
&nbsp;&nbsp;Thanks to Paul Reuvers for answering my questions
#####Daniel Palloks
&nbsp;&nbsp;Helped me to fix my Uhr implementation bug! :-)<BR>
&nbsp;&nbsp;Did some excellent testing and found a couple of annoying bugs, which are fixed now :-)<BR>
&nbsp;&nbsp;His awesome Javascript emulator can be found here: https://people.physik.hu-berlin.de/~palloks/js/enigma/index_en.html
#####David Hamer
&nbsp;&nbsp;Helping me out with questions regarding the stepping mechanism of the rotors

#####General  
Be aware that I tried my best to emulate everything as precise as I could, based on the information I could find,
but this application will definitely contain bugs and you will come across situations where a message deciphered by this
emulator could not be deciphered by some other emulator and vice versa.
I gave this application to the general public, so anyone can fiddle around with it. You may copy/paste/use anything you
find in this application, better yet: you can do anything you want with it.
I just hope that you will like it.
If you want to use anything from this application for commercial purposes, I will be last one to stop you, but I will not like you.

######Emulated machines:

* Enigma D (Commercial Enigma A26)
* Enigma I
* Norway Enigma (Norenigma)
* Enigma M3 (3-wheel Naval Enigma)
* Enigma M4 (U-Boot Enigma)
* Railway Enigma (Railway variant of Enigma K (A27))
* Swiss-K (Enigma K variant for Switzerland)
* Enigma T (Tirpitz, The Japanese Enigma)
* Enigma KD (Commercial Enigma (K) with UKW-D)
* Zahlwerk Enigma (Commercial Enigma A28) serial A-865
* Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-312
* Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-260
* Enigma G aka Abwehr Enigma (Zahlwerk Enigma G31) serial G-111
* Custom Enigma (DIY Enigma)

######Special functionality:
* UKW-D
* The Uhr
* Lückenfüller rotors support

`KVMXU KJFVB EXQUP KHKUO C`

#####Version history

######v1 build 20211210.1
First build with no changes, just created after adding this Simulator to Github again.

######v1 build 20160911.273
Various fixes and corrected the sample configuration for the Custom Enigma

######v1 build 20160911.264
- Updated sample configuration file for Custom Enigma (sample is the actual Enigma I)
- Minor setting update (capacity)

######v1 build 20160911.262
- Added BP and FRA notation for UKW-D reflector
- Added support for your own customized Enigma machines (build your own Enigma, with all the actual physical limitations!)
- Generic rotation scheme of rotors is implemented now
- Easter-egg removed (now the graphical keyboard is an available command (kbd))
- Thanks again for Daniel Palloks for supporting my efforts in building a generic Enigma machine and making sure that
  I don't give up ;-)
  
######v1 build 20160720.104
- Ringsettings can now also be changed when the machine is open (thanks Daniel! :-))
Have you found the easter-egg yet? If you have, you can use the unlisted command 'kbd'.
Hint: you must reach the value 13 by opening/closing the machine. Closing the machine adds 11 to
the current value, opening the machine subtracts 7. The value starts at 0.
The usage of prime numbers is done on purpose (decrease the number of possibilities).
It should not be difficult though.

######v1 build 20151009.103
- M3 machine did not contain the UKW-A, that's fixed now

######v1 build 20150313.99
- Added `kb` command: this opens a small dialog (the only GUI part in the simulator atm) where you can type a message, which is directly processed by the current Enigma machine character by character and you directly see the translated character after a keypress;
- Ring-setting update: instead of only using numerical input the simulator now supports alphabetic input (the same as for the positions) in case the Enigma machine you chose uses an alphabet on the rotors;
