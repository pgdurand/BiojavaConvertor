#Bioinformatics SeqVertor

[![License](https://img.shields.io/badge/license-Affero%20GPL%203.0-blue.svg)](https://www.gnu.org/licenses/agpl-3.0.txt)

##Introduction

This package is a library to enable the handling of sequence standard formats through Biojava: Genbank, Embl, Uniprot and Fasta sequence formats.

The major purpose of the package targets the instantiation of annotated sequence objects ready to be displayed in viewers available from [Bioinformatics UI library](https://github.com/pgdurand/Bioinformatics-UI-API).

##Requirements

Use a [Java Virtual Machine](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 1.7 (or above) from Oracle. 

*Not tested with any other JVM providers but Oracle... so there is no guarantee that the software will work as expected if not using Oracle's JVM.*

##Library uses

Since this package is a library, its primary purpose targets a use within other softwares. 

JUint test classes can actually be reviewed to see how to prepare DSequence and FeatureTable objects to be used in viewers available from [Bioinformatics UI library](https://github.com/pgdurand/Bioinformatics-UI-API):

![EmblReaderTest](src/test/EmblReaderTest.java): see how to read EMBL sequence format

![GenbankReaderTest](src/test/GenbankReaderTest.java): see how to read Genbank sequence format

![UniprotReaderTest](src/test/UniprotReaderTest.java): see how to read Uniprot sequence format

##License and dependencies

Bioinformatics SeqVertor API itself is released under the GNU Affero General Public License, Version 3.0. [AGPL](https://www.gnu.org/licenses/agpl-3.0.txt)

It depends on several thrid-party libraries as stated in the NOTICE.txt file provided with this project.

--
(c) 2006-2016 - Patrick G. Durand
