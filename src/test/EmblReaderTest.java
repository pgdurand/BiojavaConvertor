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
package test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bzh.plealog.bioinfo.api.core.config.CoreSystemConfigurator;
import bzh.plealog.bioinfo.seqvertor.BiojavaUtils;
import bzh.plealog.bioinfo.seqvertor.SequenceDataBag;

public class EmblReaderTest {

  private static final String emblFile = "./data/DL233279-pat.embl";
  private static final String sequence =
      "gttttgtttgttggagaattgcgtagaggggttatttctacgtgaggatccatcactaggcggtgtgggttacctccctgggtagctacatggtggtgtagggacacctgta"+
      "gtccgggttgagtgacgcatctcggactggggacctctggcgaattaagtagctaagtccgctgctttcagctctgcgcatttcatttgagcccccgctcagtttgctagca"+
      "agacccggcacatggttcgccgttaccatggatttcgaaagaaacactctgttaggtggtatcgtggatgacgcacgcagggagaggctaaggcttatcgtacgctgatctc"+
      "cgtgaatgtctacacattcctctacaggaccc";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    // required to use the Plealog Bioinformatics library
    CoreSystemConfigurator.initializeSystem();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGenbankBasic(){
    //read sequence sample
    SequenceDataBag sdb = BiojavaUtils.readEmblEntry(new File(emblFile));

    //...then check that content is ok
    assertTrue(sdb.getSeqInfo().getId().equals("DL233279"));
    assertTrue(sdb.getSeqInfo().getDescription().equals("Agent and method for identifying kind of plant."));
    assertTrue(sdb.getSeqInfo().getCreationDate()==20081126);
    assertTrue(sdb.getSeqInfo().getDivision().equals("VRL"));
    assertTrue(sdb.getSeqInfo().getMoltype().equals("unassigned RNA"));
    assertTrue(sdb.getSeqInfo().getOrganism().equals("Cucumber mosaic virus (cucumber mosaic cucumovirus)"));
    assertTrue(sdb.getSeqInfo().getTaxonomy().equals("Viruses;ssRNA positive-strand viruses;no DNA stage;Bromoviridae;Cucumovirus"));
    assertTrue(sdb.getSeqInfo().getSequenceSize()==368);
    assertTrue(sdb.getSeqInfo().getUpdateDate()==20090415);

    assertTrue(sdb.getSequence().length()==368);
    assertTrue(sdb.getSequence().equals(sequence));
    assertTrue(sdb.getFeatTable().features()==1);
    
    // Of note, the following objects are then ready to be used with Bioinformatics-UI viewers
    // (https://github.com/pgdurand/Bioinformatics-UI-API):
    //sdb.getDSequence(); // a sequence object
    //sdb.getFeatTable(); // its feature table
  }

}
