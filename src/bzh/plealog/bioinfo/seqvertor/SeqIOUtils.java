/* Copyright (C) 2006-2016 Patrick G. Durand
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.txt
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 */
package bzh.plealog.bioinfo.seqvertor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.io.FastaFormat;

/**
 * This class contains utility methods to handle sequence file data formats.
 * 
 * @author Patrick G. Durand
 */
@SuppressWarnings("deprecation")
public class SeqIOUtils {

	public static final int			UNKNOWN							= 0;
	public static final int			SWISSPROT						= 1;
	public static final int			GENBANK							= 2;
	public static final int			EMBL							= 3;
	public static final int			FASTAPROT						= 4;
	public static final int			FASTADNA						= 5;
	public static final int			FASTARNA						= 6;
	public static final int			GENPEPT							= 7;
	public static final int			FASTQ							= 8;

  public static int guessFileFormat(String fname) {
    int fileType = UNKNOWN;
    StringBuffer sequenceChecked = new StringBuffer();
    if (isFastaQ(fname)) {
      fileType = FASTQ;
    } else if (isFastaNuc(fname, sequenceChecked)) {
      if (isFastaDNA(sequenceChecked.toString())) {
        fileType = FASTADNA;
      } else {
        fileType = FASTARNA;
      }
    } else if (isFastaProt(fname)) {
      fileType = FASTAPROT;
    } else if (isGenbank(fname)) {
      fileType = GENBANK;
    } else if (isGenpept(fname)) {
      fileType = GENPEPT;
    } else if (isUniProt(fname)) {
      fileType = SWISSPROT;
    } else if (isEMBL(fname)) {
      fileType = EMBL;
    }

    return fileType;
  }
  public static String getDescription(Sequence seq, int seqType, boolean ncbiIdType) {
    Annotation annot;
    String desc = null;

    annot = seq.getAnnotation();
    try {
      switch (seqType) {
      case SeqIOUtils.EMBL:
      case SeqIOUtils.SWISSPROT:
        desc = getStringData(annot.getProperty("DE"));
        break;
      case SeqIOUtils.GENPEPT:
      case SeqIOUtils.GENBANK:
        desc = getStringData(annot.getProperty("DEFINITION"));
        break;
      case SeqIOUtils.FASTADNA:
      case SeqIOUtils.FASTARNA:
      case SeqIOUtils.FASTAPROT:
        desc = getStringData(annot.getProperty("description"));
        if (desc == null)
          desc = seq.getName();
        break;
      }
    } catch (NoSuchElementException e) {
      desc = null;
    }
    return desc;
  }
	 /**
   * This method analyzes an ID given the NCBI recommendations.
   */
  public static String getId(String id, boolean ncbiIdType) {
    int idx;
    if (ncbiIdType) {
      //official NCBI id: return it!
      if (id.startsWith("gi|") || id.startsWith("lcl|")) {
        return id;
      }
      //get ID located between first and second '|'
      //except if ID starts with gnl. In this case, second string
      //is the DB identifier, and seqID is the third string.
      //see table 1.1 from
      //http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/formatdb_fastacmd.html
      if (id.startsWith("gnl")) {
        id = id.substring(id.indexOf('|') + 1);
      }
      idx = id.indexOf('|');
      if (idx != -1) {
        id = id.substring(idx + 1);
        idx = id.indexOf('|');
        if (idx != -1) {
          id = id.substring(0, idx);
        }
      }
    }
    return id;
  }
  public static String getId(Sequence seq, boolean ncbiIdType) {
    return getId(seq.getName(), ncbiIdType);
  }
  @SuppressWarnings("rawtypes")
  public static String getStringData(Object data) {
    if (data instanceof List) {
      List lst = (List) data;
      StringBuffer buf = new StringBuffer();
      int i, size;

      size = lst.size();
      for (i = 0; i < size; i++) {
        buf.append(lst.get(i));
        if ((i + 1) < size)
          buf.append(" ");
      }
      return buf.toString();
    } else {
      return data.toString();
    }
  }

  public static String getIdentifier(Sequence seq, int seqType, boolean ncbiIdType) {
    Annotation annot;
    String desc = null;
    String id;
    annot = seq.getAnnotation();
    try {
      switch (seqType) {
      case SeqIOUtils.EMBL:
      case SeqIOUtils.SWISSPROT:
        desc = getStringData(annot.getProperty("DE"));
        break;
      case SeqIOUtils.GENPEPT:
      case SeqIOUtils.GENBANK:
        desc = getStringData(annot.getProperty("DEFINITION"));
        break;
      case SeqIOUtils.FASTADNA:
      case SeqIOUtils.FASTARNA:
      case SeqIOUtils.FASTAPROT:
        desc = getStringData(annot.getProperty("description"));
        if (desc == null)
          desc = seq.getName();
        break;
      }
    } catch (NoSuchElementException e) {
      desc = null;
    }
    id = getId(seq, ncbiIdType);
    /*idx = id.indexOf('|');
    if (idx==-1){
      id = "lcl|"+id;
    }
    else if (idx>3){
      id = "lcl|"+id;
    }*/
    if (desc == null)
      annot.setProperty(FastaFormat.PROPERTY_DESCRIPTIONLINE, id);
    else
      annot.setProperty(FastaFormat.PROPERTY_DESCRIPTIONLINE, id + " " + desc);
    return id;
  }

  /**
   * Utility method. Read up to 50 line of a fasta file and returns the sequence read.
   */
  private static String readFastaPartial(String fname, char idKey) {
    StringBuffer buf;
    String line;
    boolean reading = false;
    int readCounter = 0, lineCounter = 0;

    buf = new StringBuffer();
    try (BufferedReader reader = new BufferedReader(new FileReader(fname));) {
      while ((line = reader.readLine()) != null) {
        if (line.length() == 0)
          continue;
        lineCounter++;
        if (lineCounter > 100)//read up to 100 lines max to find a idKey (> for Fasta, @ for FastQ)
          break;
        if (line.charAt(0) == idKey) {
          if (reading)
            break;
          if (!reading) {
            reading = true;
            continue;
          }
        }
        if (reading) {
          readCounter++;
          buf.append(line);
          //FastQ: sequence is on a single line
          if (idKey == '@' && line.startsWith("+")) {
            break;
          }
          if (readCounter > 10)//read up to 10 lines max to get a sequence peace
            break;
        }
      }
    } catch (Exception e) {//should not happen
    }

    return buf.toString();
  }

  /**
   * Basic method that checks whether the sequence is a protein. Return true if this sequence contains a letter that
   * differs from ACGTNUX.
   */
  public static boolean isProteic(String sequence) {
    int i, size;
    size = sequence.length();
    sequence = sequence.toUpperCase();
    for (i = 0; i < size; i++) {
      if ("ACGTNUX".indexOf(sequence.charAt(i)) == -1)
        return true;
    }
    return false;
  }

  /**
   * Method to check if the parameter sequence is DNA or RNA 
   * 
   * @param sequence
   * 
   * @return true if the sequence contains characters 'T'
   */
  public static boolean isFastaDNA(String sequence) {
    if (!(sequence==null || sequence.length()==0)) {
      return sequence.toUpperCase().contains("T");
    }
    return false;
  }

  /**
   * Figures out if a file is a fasta protein sequence file.
   */
  public static boolean isFastaProt(String fName) {
    String seq;

    seq = readFastaPartial(fName, '>');
    if (seq.length() == 0)
      return false;
    return isProteic(seq);
  }

  /**
   * Figures out if a file is a fasta nucleotide sequence file.
   * And store the checked sequence in the buffer if not null
   */
  public static boolean isFastaNuc(String fName, StringBuffer sequenceBuffer) {
    String seq;

    seq = readFastaPartial(fName, '>');
    if (seq.length() == 0)
      return false;
    if (sequenceBuffer != null) {
      sequenceBuffer.append(seq);
    }
    return !isProteic(seq);
  }

  /**
   * Figures out if a file is a FastQ nucleotide sequence file.
   */
  public static boolean isFastaQ(String fName) {
    String seq;

    seq = readFastaPartial(fName, '@');
    if (seq.length() == 0)
      return false;
    //return !isProteic(seq.toUpperCase());
    //we do not control FastQ letters since we can have extended Nuc UIPAC codes
    return true;
  }

  /**
   * Utility method. Checker if a file is of a given molecular type. Used to check Embl/Genbanl/Genpept file to check
   * if it contains AA or BP key.
   */
  private static boolean checkfile(String file, String idKey, String molType) {
    BufferedReader reader = null;
    String line, id = null;
    int counter = 0;

    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
      while ((line = reader.readLine()) != null) {
        if (counter > 50)
          break;
        if (line.startsWith(idKey)) {
          id = line.toUpperCase().trim();
          break;
        }
        counter++;
      }

    } catch (Exception e) {
    }
    if (reader != null) {
      try {
        reader.close();
      } catch (IOException e) {
      }
    }
    if (id == null)
      return false;
    return id.indexOf(molType) != -1;
  }

  /**
   * Figures out if a file is from Uniprot.
   */
  public static boolean isUniProt(String file) {
    return checkfile(file, SeqFileParserKeys.KEYWORD_ID, SeqFileParserKeys.KEYWORD_NB_LETTERS + ".");
  }

  /**
   * Figures out if a file is from Embl.
   */
  public static boolean isEMBL(String file) {
    return checkfile(file, "ID", " BP.");
  }

  /**
   * Figures out if a file is from Genbank.
   */
  public static boolean isGenbank(String file) {
    return checkfile(file, "LOCUS", " BP ");
  }

  /**
   * Figures out if a file is from Genpept.
   */
  public static boolean isGenpept(String file) {
    return checkfile(file, "LOCUS", " AA ");
  }

}
