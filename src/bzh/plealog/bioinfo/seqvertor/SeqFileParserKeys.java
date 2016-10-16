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

/**
 * This class defines some constants for standard data format.
 * 
 * @author Patrick G. Durand
 */
public class SeqFileParserKeys {

  public static final String KEYWORD_ID = "ID";
  public static final String KEYWORD_ACCESSION = "AC";
  public static final String KEYWORD_ORGANISM = "OS";
  public static final String KEYWORD_ORGANISM_ID = "OX";
  public static final String KEYWORD_FEATURE_TABLE = "FT";
  public static final String KEYWORD_START_SEQUENCE = "SQ";
  public static final String KEYWORD_NB_LETTERS = "AA";
  public static final String KEYWORD_END_ENTRY = "//";
}
