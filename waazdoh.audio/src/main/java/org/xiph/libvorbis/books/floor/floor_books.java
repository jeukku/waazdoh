/********************************************************************
 *                                                                  *
 * THIS FILE IS PART OF THE OggVorbis SOFTWARE CODEC SOURCE CODE.   *
 * USE, DISTRIBUTION AND REPRODUCTION OF THIS LIBRARY SOURCE IS     *
 * GOVERNED BY A BSD-STYLE SOURCE LICENSE INCLUDED WITH THIS SOURCE *
 * IN 'COPYING'. PLEASE READ THESE TERMS BEFORE DISTRIBUTING.       *
 *                                                                  *
 * THE OggVorbis SOURCE CODE IS (C) COPYRIGHT 1994-2002             *
 * by the Xiph.Org Foundation http://www.xiph.org/                  *
 *                                                                  *
 ********************************************************************/

package org.xiph.libvorbis.books.floor;

import org.xiph.libvorbis.*;

public class floor_books {

	// _floor_128x4_books
	static int[] _huff_lengthlist_line_128x4_class0 = { 7, 7, 7, 11, 6, 6, 7,
			11, 7, 6, 6, 10, 12, 10, 10, 13, 7, 7, 8, 11, 7, 7, 7, 11, 7, 6, 7,
			10, 11, 10, 10, 13, 10, 10, 9, 12, 9, 9, 9, 11, 8, 8, 8, 11, 13,
			11, 10, 14, 15, 15, 14, 15, 15, 14, 13, 14, 15, 12, 12, 17, 17, 17,
			17, 17, 7, 7, 6, 9, 6, 6, 6, 9, 7, 6, 6, 8, 11, 11, 10, 12, 7, 7,
			7, 9, 7, 6, 6, 9, 7, 6, 6, 9, 13, 10, 10, 11, 10, 9, 8, 10, 9, 8,
			8, 10, 8, 8, 7, 9, 13, 12, 10, 11, 17, 14, 14, 13, 15, 14, 12, 13,
			17, 13, 12, 15, 17, 17, 14, 17, 7, 6, 6, 7, 6, 6, 5, 7, 6, 6, 6, 6,
			11, 9, 9, 9, 7, 7, 6, 7, 7, 6, 6, 7, 6, 6, 6, 6, 10, 9, 8, 9, 10,
			9, 8, 8, 9, 8, 7, 8, 8, 7, 6, 8, 11, 10, 9, 10, 17, 17, 12, 15, 15,
			15, 12, 14, 14, 14, 10, 12, 15, 13, 12, 13, 11, 10, 8, 10, 11, 10,
			8, 8, 10, 9, 7, 7, 10, 9, 9, 11, 11, 11, 9, 10, 11, 10, 8, 9, 10,
			8, 6, 8, 10, 9, 9, 11, 14, 13, 10, 12, 12, 11, 10, 10, 8, 7, 8, 10,
			10, 11, 11, 12, 17, 17, 15, 17, 17, 17, 17, 17, 17, 13, 12, 17, 17,
			17, 14, 17, };

	static static_codebook _huff_book_line_128x4_class0 = new static_codebook(
			1, 256, _huff_lengthlist_line_128x4_class0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x4_0sub0 = { 2, 2, 2, 2, };

	static static_codebook _huff_book_line_128x4_0sub0 = new static_codebook(1,
			4, _huff_lengthlist_line_128x4_0sub0, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x4_0sub1 = { 0, 0, 0, 0, 3, 2, 3, 2,
			3, 3, };

	static static_codebook _huff_book_line_128x4_0sub1 = new static_codebook(1,
			10, _huff_lengthlist_line_128x4_0sub1, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x4_0sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 3, 3, 4, 3, 4, 3, 4, 4, 5, 4, 5, 4, 6, 5, 6, };

	static static_codebook _huff_book_line_128x4_0sub2 = new static_codebook(1,
			25, _huff_lengthlist_line_128x4_0sub2, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x4_0sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 3, 5, 3,
			5, 3, 5, 4, 6, 5, 6, 5, 7, 6, 6, 7, 7, 9, 9, 11, 11, 16, 11, 14,
			10, 11, 11, 13, 16, 15, 15, 15, 15, 15, 15, 15, 15, 15, };

	static static_codebook _huff_book_line_128x4_0sub3 = new static_codebook(1,
			64, _huff_lengthlist_line_128x4_0sub3, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	// _floor_256x4_books
	static int[] _huff_lengthlist_line_256x4_class0 = { 6, 7, 7, 12, 6, 6, 7,
			12, 7, 6, 6, 10, 15, 12, 11, 13, 7, 7, 8, 13, 7, 7, 8, 12, 7, 7, 7,
			11, 12, 12, 11, 13, 10, 9, 9, 11, 9, 9, 9, 10, 10, 8, 8, 12, 14,
			12, 12, 14, 11, 11, 12, 14, 11, 12, 11, 15, 15, 12, 13, 15, 15, 15,
			15, 15, 6, 6, 7, 10, 6, 6, 6, 11, 7, 6, 6, 9, 14, 12, 11, 13, 7, 7,
			7, 10, 6, 6, 7, 9, 7, 7, 6, 10, 13, 12, 10, 12, 9, 9, 9, 11, 9, 9,
			8, 9, 9, 8, 8, 10, 13, 12, 10, 12, 12, 12, 11, 13, 12, 12, 11, 12,
			15, 13, 12, 15, 15, 15, 14, 14, 6, 6, 6, 8, 6, 6, 5, 6, 7, 7, 6, 5,
			11, 10, 9, 8, 7, 6, 6, 7, 6, 6, 5, 6, 7, 7, 6, 6, 11, 10, 9, 8, 8,
			8, 8, 9, 8, 8, 7, 8, 8, 8, 6, 7, 11, 10, 9, 9, 14, 11, 10, 14, 14,
			11, 10, 15, 13, 11, 9, 11, 15, 12, 12, 11, 11, 9, 8, 8, 10, 9, 8,
			9, 11, 10, 9, 8, 12, 11, 12, 11, 13, 10, 8, 9, 11, 10, 8, 9, 10, 9,
			8, 9, 10, 8, 12, 12, 15, 11, 10, 10, 13, 11, 10, 10, 8, 8, 7, 12,
			10, 9, 11, 12, 15, 12, 11, 15, 13, 11, 11, 15, 12, 14, 11, 13, 15,
			15, 13, 13, };

	static static_codebook _huff_book_line_256x4_class0 = new static_codebook(
			1, 256, _huff_lengthlist_line_256x4_class0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_256x4_0sub0 = { 2, 2, 2, 2, };

	static static_codebook _huff_book_line_256x4_0sub0 = new static_codebook(1,
			4, _huff_lengthlist_line_256x4_0sub0, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x4_0sub1 = { 0, 0, 0, 0, 2, 2, 3, 3,
			3, 3, };

	static static_codebook _huff_book_line_256x4_0sub1 = new static_codebook(1,
			10, _huff_lengthlist_line_256x4_0sub1, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x4_0sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 4, 3, 4, 3, 4, 3, 5, 3, 5, 4, 5, 4, 6, 4, 6, };

	static static_codebook _huff_book_line_256x4_0sub2 = new static_codebook(1,
			25, _huff_lengthlist_line_256x4_0sub2, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x4_0sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 3, 5, 3,
			5, 3, 6, 4, 7, 4, 7, 5, 7, 6, 7, 6, 7, 8, 10, 13, 13, 13, 13, 13,
			13, 13, 13, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, };

	static static_codebook _huff_book_line_256x4_0sub3 = new static_codebook(1,
			64, _huff_lengthlist_line_256x4_0sub3, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	// _floor_128x7_books
	static int[] _huff_lengthlist_line_128x7_class0 = { 10, 7, 8, 13, 9, 6, 7,
			11, 10, 8, 8, 12, 17, 17, 17, 17, 7, 5, 5, 9, 6, 4, 4, 8, 8, 5, 5,
			8, 16, 14, 13, 16, 7, 5, 5, 7, 6, 3, 3, 5, 8, 5, 4, 7, 14, 12, 12,
			15, 10, 7, 8, 9, 7, 5, 5, 6, 9, 6, 5, 5, 15, 12, 9, 10, };

	static static_codebook _huff_book_line_128x7_class0 = new static_codebook(
			1, 64, _huff_lengthlist_line_128x7_class0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x7_class1 = { 8, 13, 17, 17, 8, 11,
			17, 17, 11, 13, 17, 17, 17, 17, 17, 17, 6, 10, 16, 17, 6, 10, 15,
			17, 8, 10, 16, 17, 17, 17, 17, 17, 9, 13, 15, 17, 8, 11, 17, 17,
			10, 12, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17,
			17, 17, 17, 17, 17, 17, 17, 6, 11, 15, 17, 7, 10, 15, 17, 8, 10,
			17, 17, 17, 15, 17, 17, 4, 8, 13, 17, 4, 7, 13, 17, 6, 8, 15, 17,
			16, 15, 17, 17, 6, 11, 15, 17, 6, 9, 13, 17, 8, 10, 17, 17, 15, 17,
			17, 17, 16, 17, 17, 17, 12, 14, 15, 17, 13, 14, 15, 17, 17, 17, 17,
			17, 5, 10, 14, 17, 5, 9, 14, 17, 7, 9, 15, 17, 15, 15, 17, 17, 3,
			7, 12, 17, 3, 6, 11, 17, 5, 7, 13, 17, 12, 12, 17, 17, 5, 9, 14,
			17, 3, 7, 11, 17, 5, 8, 13, 17, 13, 11, 16, 17, 12, 17, 17, 17, 9,
			14, 15, 17, 10, 11, 14, 17, 16, 14, 17, 17, 8, 12, 17, 17, 8, 12,
			17, 17, 10, 12, 17, 17, 17, 17, 17, 17, 5, 10, 17, 17, 5, 9, 15,
			17, 7, 9, 17, 17, 13, 13, 17, 17, 7, 11, 17, 17, 6, 10, 15, 17, 7,
			9, 15, 17, 12, 11, 17, 17, 12, 15, 17, 17, 11, 14, 17, 17, 11, 10,
			15, 17, 17, 16, 17, 17, };

	static static_codebook _huff_book_line_128x7_class1 = new static_codebook(
			1, 256, _huff_lengthlist_line_128x7_class1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x7_0sub1 = { 0, 3, 3, 3, 3, 3, 3, 3,
			3, };

	static static_codebook _huff_book_line_128x7_0sub1 = new static_codebook(1,
			9, _huff_lengthlist_line_128x7_0sub1, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x7_0sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 3, 3, 3, 4, 4, 4, 4, 5, 4, 5, 4, 5, 4, 6, 4, 6, };

	static static_codebook _huff_book_line_128x7_0sub2 = new static_codebook(1,
			25, _huff_lengthlist_line_128x7_0sub2, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x7_0sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 3, 5, 3,
			5, 4, 5, 4, 5, 5, 5, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 7, 8, 9, 11,
			13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, };

	static static_codebook _huff_book_line_128x7_0sub3 = new static_codebook(1,
			64, _huff_lengthlist_line_128x7_0sub3, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x7_1sub1 = { 0, 3, 3, 2, 3, 3, 4, 3,
			4, };

	static static_codebook _huff_book_line_128x7_1sub1 = new static_codebook(1,
			9, _huff_lengthlist_line_128x7_1sub1, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x7_1sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 3, 4, 3, 6, 3, 6, 3, 6, 3, 7, 3, 8, 4, 9, 4, 9, };

	static static_codebook _huff_book_line_128x7_1sub2 = new static_codebook(1,
			25, _huff_lengthlist_line_128x7_1sub2, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_128x7_1sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 7, 2, 7, 3,
			8, 4, 9, 5, 9, 8, 10, 11, 11, 12, 14, 14, 14, 14, 14, 14, 14, 14,
			14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 13, 13, 13, 13, };

	static static_codebook _huff_book_line_128x7_1sub3 = new static_codebook(1,
			64, _huff_lengthlist_line_128x7_1sub3, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	// _floor_256x7_books
	static int[] _huff_lengthlist_line_256x7_0sub1 = { 0, 2, 3, 3, 3, 3, 4, 3,
			4, };

	static static_codebook _huff_book_line_256x7_0sub1 = new static_codebook(1,
			9, _huff_lengthlist_line_256x7_0sub1, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x7_0sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 3, 4, 3, 4, 3, 5, 3, 6, 3, 6, 4, 6, 4, 7, 5, 7, };

	static static_codebook _huff_book_line_256x7_0sub2 = new static_codebook(1,
			25, _huff_lengthlist_line_256x7_0sub2, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x7_0sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 2, 5, 3,
			5, 3, 6, 3, 6, 4, 7, 6, 7, 8, 7, 9, 8, 9, 9, 9, 10, 9, 11, 13, 11,
			13, 10, 10, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, };

	static static_codebook _huff_book_line_256x7_0sub3 = new static_codebook(1,
			64, _huff_lengthlist_line_256x7_0sub3, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x7_1sub1 = { 0, 3, 3, 3, 3, 2, 4, 3,
			4, };

	static static_codebook _huff_book_line_256x7_1sub1 = new static_codebook(1,
			9, _huff_lengthlist_line_256x7_1sub1, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x7_1sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 2, 3, 3, 4, 3, 4, 4, 5, 4, 6, 5, 6, 7, 6, 8, 8, };

	static static_codebook _huff_book_line_256x7_1sub2 = new static_codebook(1,
			25, _huff_lengthlist_line_256x7_1sub2, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x7_1sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 4, 3, 6,
			3, 7, 3, 8, 5, 8, 6, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
			8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7, };

	static static_codebook _huff_book_line_256x7_1sub3 = new static_codebook(1,
			64, _huff_lengthlist_line_256x7_1sub3, 0, 0, 0, 0, 0, null, null,
			null, null, 0);

	static int[] _huff_lengthlist_line_256x7_class0 = { 7, 5, 5, 9, 9, 6, 6, 9,
			12, 8, 7, 8, 11, 8, 9, 15, 6, 3, 3, 7, 7, 4, 3, 6, 9, 6, 5, 6, 8,
			6, 8, 15, 8, 5, 5, 9, 8, 5, 4, 6, 10, 7, 5, 5, 11, 8, 7, 15, 14,
			15, 13, 13, 13, 13, 8, 11, 15, 10, 7, 6, 11, 9, 10, 15, };

	static static_codebook _huff_book_line_256x7_class0 = new static_codebook(
			1, 64, _huff_lengthlist_line_256x7_class0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_256x7_class1 = { 5, 6, 8, 15, 6, 9, 10,
			15, 10, 11, 12, 15, 15, 15, 15, 15, 4, 6, 7, 15, 6, 7, 8, 15, 9, 8,
			9, 15, 15, 15, 15, 15, 6, 8, 9, 15, 7, 7, 8, 15, 10, 9, 10, 15, 15,
			15, 15, 15, 15, 13, 15, 15, 15, 10, 11, 15, 15, 13, 13, 15, 15, 15,
			15, 15, 4, 6, 7, 15, 6, 8, 9, 15, 10, 10, 12, 15, 15, 15, 15, 15,
			2, 5, 6, 15, 5, 6, 7, 15, 8, 6, 7, 15, 15, 15, 15, 15, 5, 6, 8, 15,
			5, 6, 7, 15, 9, 6, 7, 15, 15, 15, 15, 15, 14, 12, 13, 15, 12, 10,
			11, 15, 15, 15, 15, 15, 15, 15, 15, 15, 7, 8, 9, 15, 9, 10, 10, 15,
			15, 14, 14, 15, 15, 15, 15, 15, 5, 6, 7, 15, 7, 8, 9, 15, 12, 9,
			10, 15, 15, 15, 15, 15, 7, 7, 9, 15, 7, 7, 8, 15, 12, 8, 9, 15, 15,
			15, 15, 15, 13, 13, 14, 15, 12, 11, 12, 15, 15, 15, 15, 15, 15, 15,
			15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
			15, 13, 13, 13, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
			15, 12, 13, 15, 15, 12, 13, 15, 15, 14, 15, 15, 15, 15, 15, 15, 15,
			15, 15, 15, 15, 15, 13, 15, 15, 15, 15, 15, 15, 15, 15, 15, };

	static static_codebook _huff_book_line_256x7_class1 = new static_codebook(
			1, 256, _huff_lengthlist_line_256x7_class1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	// _floor_128x11_books
	static int[] _huff_lengthlist_line_128x11_class1 = { 1, 6, 3, 7, 2, 4, 5,
			7, };

	static static_codebook _huff_book_line_128x11_class1 = new static_codebook(
			1, 8, _huff_lengthlist_line_128x11_class1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_class2 = { 1, 6, 12, 16, 4, 12,
			15, 16, 9, 15, 16, 16, 16, 16, 16, 16, 2, 5, 11, 16, 5, 11, 13, 16,
			9, 13, 16, 16, 16, 16, 16, 16, 4, 8, 12, 16, 5, 9, 12, 16, 9, 13,
			15, 16, 16, 16, 16, 16, 15, 16, 16, 16, 11, 14, 13, 16, 12, 15, 16,
			16, 16, 16, 16, 15, };

	static static_codebook _huff_book_line_128x11_class2 = new static_codebook(
			1, 64, _huff_lengthlist_line_128x11_class2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_class3 = { 7, 6, 9, 17, 7, 6, 8,
			17, 12, 9, 11, 16, 16, 16, 16, 16, 5, 4, 7, 16, 5, 3, 6, 14, 9, 6,
			8, 15, 16, 16, 16, 16, 5, 4, 6, 13, 3, 2, 4, 11, 7, 4, 6, 13, 16,
			11, 10, 14, 12, 12, 12, 16, 9, 7, 10, 15, 12, 9, 11, 16, 16, 15,
			15, 16, };

	static static_codebook _huff_book_line_128x11_class3 = new static_codebook(
			1, 64, _huff_lengthlist_line_128x11_class3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_0sub0 = { 5, 5, 5, 5, 5, 5, 6, 5,
			6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 6, 6, 6,
			7, 6, 7, 6, 7, 6, 7, 6, 7, 6, 7, 6, 8, 6, 8, 6, 8, 7, 8, 7, 8, 7,
			8, 7, 9, 7, 9, 8, 9, 8, 9, 8, 10, 8, 10, 9, 10, 9, 10, 9, 11, 9,
			11, 9, 10, 10, 11, 10, 11, 10, 11, 11, 11, 11, 11, 11, 12, 13, 14,
			14, 14, 15, 15, 16, 16, 16, 17, 15, 16, 15, 16, 16, 17, 17, 16, 17,
			17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17,
			17, 17, 17, 17, 17, };

	static static_codebook _huff_book_line_128x11_0sub0 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x11_0sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_1sub0 = { 2, 5, 5, 5, 5, 5, 5, 4,
			5, 5, 5, 5, 5, 5, 5, 5, 6, 5, 6, 5, 6, 5, 7, 6, 7, 6, 7, 6, 8, 6,
			8, 6, };

	static static_codebook _huff_book_line_128x11_1sub0 = new static_codebook(
			1, 32, _huff_lengthlist_line_128x11_1sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_1sub1 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 5, 3, 5, 3, 6, 4, 6, 4, 7, 4, 7, 4, 7, 4, 8, 4, 8, 4, 9, 5,
			9, 5, 9, 5, 9, 6, 10, 6, 10, 6, 11, 7, 10, 7, 10, 8, 11, 9, 11, 9,
			11, 10, 11, 11, 12, 11, 11, 12, 15, 15, 12, 14, 11, 14, 12, 14, 11,
			14, 13, 14, 12, 14, 11, 14, 11, 14, 12, 14, 11, 14, 11, 14, 13, 13,
			14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
			14, 14, 14, 14, 14, };

	static static_codebook _huff_book_line_128x11_1sub1 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x11_1sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_2sub1 = { 0, 4, 5, 4, 5, 4, 5, 3,
			5, 3, 5, 3, 5, 4, 4, 4, 5, 5, };

	static static_codebook _huff_book_line_128x11_2sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_128x11_2sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_2sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 4, 4, 4, 4, 5, 4, 5, 4, 6,
			5, 7, 5, 7, 6, 8, 6, 8, 6, 9, 7, 9, 7, 10, 7, 9, 8, 11, 8, 11, };

	static static_codebook _huff_book_line_128x11_2sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_128x11_2sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_2sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 8,
			3, 8, 4, 8, 4, 8, 6, 8, 5, 8, 4, 8, 4, 8, 6, 8, 7, 8, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, };

	static static_codebook _huff_book_line_128x11_2sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x11_2sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_3sub1 = { 0, 4, 4, 4, 4, 4, 4, 4,
			4, 4, 4, 4, 4, 4, 5, 4, 5, 4, };

	static static_codebook _huff_book_line_128x11_3sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_128x11_3sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_3sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 3, 5, 4, 6, 4, 6, 4, 7, 4, 7, 4,
			8, 4, 8, 4, 9, 4, 9, 4, 10, 4, 10, 5, 10, 5, 11, 5, 12, 6, 12, 6, };

	static static_codebook _huff_book_line_128x11_3sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_128x11_3sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x11_3sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 1,
			6, 3, 7, 3, 8, 4, 8, 5, 8, 8, 8, 9, 7, 8, 8, 7, 7, 7, 8, 9, 10, 9,
			9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
			10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
			10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
			10, 9, 9, };

	static static_codebook _huff_book_line_128x11_3sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x11_3sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	// _floor_128x17_books
	static int[] _huff_lengthlist_line_128x17_class1 = { 1, 3, 4, 7, 2, 5, 6,
			7, };

	static static_codebook _huff_book_line_128x17_class1 = new static_codebook(
			1, 8, _huff_lengthlist_line_128x17_class1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_class2 = { 1, 4, 10, 19, 3, 8,
			13, 19, 7, 12, 19, 19, 19, 19, 19, 19, 2, 6, 11, 19, 8, 13, 19, 19,
			9, 11, 19, 19, 19, 19, 19, 19, 6, 7, 13, 19, 9, 13, 19, 19, 10, 13,
			18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
			18, 18, 18, 18, 18, };

	static static_codebook _huff_book_line_128x17_class2 = new static_codebook(
			1, 64, _huff_lengthlist_line_128x17_class2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_class3 = { 3, 6, 10, 17, 4, 8,
			11, 20, 8, 10, 11, 20, 20, 20, 20, 20, 2, 4, 8, 18, 4, 6, 8, 17, 7,
			8, 10, 20, 20, 17, 20, 20, 3, 5, 8, 17, 3, 4, 6, 17, 8, 8, 10, 17,
			17, 12, 16, 20, 13, 13, 15, 20, 10, 10, 12, 20, 15, 14, 15, 20, 20,
			20, 19, 19, };

	static static_codebook _huff_book_line_128x17_class3 = new static_codebook(
			1, 64, _huff_lengthlist_line_128x17_class3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_0sub0 = { 5, 5, 6, 5, 6, 5, 6, 5,
			6, 5, 6, 5, 6, 5, 6, 5, 7, 5, 7, 5, 7, 5, 7, 5, 7, 5, 7, 5, 8, 5,
			8, 5, 8, 5, 8, 5, 8, 6, 8, 6, 8, 6, 9, 6, 9, 6, 9, 6, 9, 6, 9, 7,
			9, 7, 9, 7, 9, 7, 10, 7, 10, 8, 10, 8, 10, 8, 10, 8, 10, 8, 11, 8,
			11, 8, 11, 8, 11, 8, 11, 9, 12, 9, 12, 9, 12, 9, 12, 9, 12, 10, 12,
			10, 13, 11, 13, 11, 14, 12, 14, 13, 15, 14, 16, 14, 17, 15, 18, 16,
			20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
			20, 20, 20, };

	static static_codebook _huff_book_line_128x17_0sub0 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x17_0sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_1sub0 = { 2, 5, 5, 4, 5, 4, 5, 4,
			5, 5, 5, 5, 5, 5, 6, 5, 6, 5, 6, 5, 7, 6, 7, 6, 7, 6, 8, 6, 9, 7,
			9, 7, };

	static static_codebook _huff_book_line_128x17_1sub0 = new static_codebook(
			1, 32, _huff_lengthlist_line_128x17_1sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_1sub1 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 4, 3, 5, 3, 5, 3, 6, 3, 6, 4, 6, 4, 7, 4, 7, 5, 8, 5, 8, 6,
			9, 7, 9, 7, 9, 8, 10, 9, 10, 9, 11, 10, 11, 11, 11, 11, 11, 11, 12,
			12, 12, 13, 12, 13, 12, 14, 12, 15, 12, 14, 12, 16, 13, 17, 13, 17,
			14, 17, 14, 16, 13, 17, 14, 17, 14, 17, 15, 17, 15, 15, 16, 17, 17,
			17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 16, 16, 16, 16,
			16, 16, 16, 16, 16, 16, };

	static static_codebook _huff_book_line_128x17_1sub1 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x17_1sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_2sub1 = { 0, 4, 5, 4, 6, 4, 8, 3,
			9, 3, 9, 2, 9, 3, 8, 4, 9, 4, };

	static static_codebook _huff_book_line_128x17_2sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_128x17_2sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_2sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 5, 3, 5, 3, 5, 4, 7, 5, 10, 7,
			10, 7, 12, 10, 14, 10, 14, 9, 14, 11, 14, 14, 14, 13, 13, 13, 13,
			13, 13, 13, };

	static static_codebook _huff_book_line_128x17_2sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_128x17_2sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_2sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
			6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
			6, 6, 6, 6, 6, 6, 6, 6, 6, 6, };

	static static_codebook _huff_book_line_128x17_2sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x17_2sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_3sub1 = { 0, 4, 4, 4, 4, 4, 4, 4,
			5, 3, 5, 3, 5, 4, 6, 4, 6, 4, };

	static static_codebook _huff_book_line_128x17_3sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_128x17_3sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_3sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 3, 6, 3, 6, 4, 7, 4, 7, 4, 7, 4,
			8, 4, 8, 4, 8, 4, 8, 4, 9, 4, 9, 5, 10, 5, 10, 7, 10, 8, 10, 8, };

	static static_codebook _huff_book_line_128x17_3sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_128x17_3sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_128x17_3sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2,
			4, 3, 4, 4, 4, 5, 4, 7, 5, 8, 5, 11, 6, 10, 6, 12, 7, 12, 7, 12, 8,
			12, 8, 12, 10, 12, 12, 12, 12, 12, 11, 11, 11, 11, 11, 11, 11, 11,
			11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
			11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
			11, 11, 11, 11, };

	static static_codebook _huff_book_line_128x17_3sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_128x17_3sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	// _floor_256x4low_books
	static int[] _huff_lengthlist_line_256x4low_class0 = { 4, 5, 6, 11, 5, 5,
			6, 10, 7, 7, 6, 6, 14, 13, 9, 9, 6, 6, 6, 10, 6, 6, 6, 9, 8, 7, 7,
			9, 14, 12, 8, 11, 8, 7, 7, 11, 8, 8, 7, 11, 9, 9, 7, 9, 13, 11, 9,
			13, 19, 19, 18, 19, 15, 16, 16, 19, 11, 11, 10, 13, 10, 10, 9, 15,
			5, 5, 6, 13, 6, 6, 6, 11, 8, 7, 6, 7, 14, 11, 10, 11, 6, 6, 6, 12,
			7, 6, 6, 11, 8, 7, 7, 11, 13, 11, 9, 11, 9, 7, 6, 12, 8, 7, 6, 12,
			9, 8, 8, 11, 13, 10, 7, 13, 19, 19, 17, 19, 17, 14, 14, 19, 12, 10,
			8, 12, 13, 10, 9, 16, 7, 8, 7, 12, 7, 7, 7, 11, 8, 7, 7, 8, 12, 12,
			11, 11, 8, 8, 7, 12, 8, 7, 6, 11, 8, 7, 7, 10, 10, 11, 10, 11, 9,
			8, 8, 13, 9, 8, 7, 12, 10, 9, 7, 11, 9, 8, 7, 11, 18, 18, 15, 18,
			18, 16, 17, 18, 15, 11, 10, 18, 11, 9, 9, 18, 16, 16, 13, 16, 12,
			11, 10, 16, 12, 11, 9, 6, 15, 12, 11, 13, 16, 16, 14, 14, 13, 11,
			12, 16, 12, 9, 9, 13, 13, 10, 10, 12, 17, 18, 17, 17, 14, 15, 14,
			16, 14, 12, 14, 15, 12, 10, 11, 12, 18, 18, 18, 18, 18, 18, 18, 18,
			18, 12, 13, 18, 16, 11, 9, 18, };

	static static_codebook _huff_book_line_256x4low_class0 = new static_codebook(
			1, 256, _huff_lengthlist_line_256x4low_class0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_256x4low_0sub0 = { 1, 3, 2, 3, };

	static static_codebook _huff_book_line_256x4low_0sub0 = new static_codebook(
			1, 4, _huff_lengthlist_line_256x4low_0sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_256x4low_0sub1 = { 0, 0, 0, 0, 2, 3, 2,
			3, 3, 3, };

	static static_codebook _huff_book_line_256x4low_0sub1 = new static_codebook(
			1, 10, _huff_lengthlist_line_256x4low_0sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_256x4low_0sub2 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 3, 3, 3, 4, 3, 4, 4, 4, 4, 4, 5, 5, 5, 6, 6, };

	static static_codebook _huff_book_line_256x4low_0sub2 = new static_codebook(
			1, 25, _huff_lengthlist_line_256x4low_0sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_256x4low_0sub3 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 2, 4,
			3, 5, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7, 7, 8, 6, 9, 7, 12, 11,
			16, 13, 16, 12, 15, 13, 15, 12, 14, 12, 15, 15, 15, };

	static static_codebook _huff_book_line_256x4low_0sub3 = new static_codebook(
			1, 64, _huff_lengthlist_line_256x4low_0sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	// _floor_1024x27_books
	static int[] _huff_lengthlist_line_1024x27_class1 = { 2, 10, 8, 14, 7, 12,
			11, 14, 1, 5, 3, 7, 4, 9, 7, 13, };

	static static_codebook _huff_book_line_1024x27_class1 = new static_codebook(
			1, 16, _huff_lengthlist_line_1024x27_class1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_class2 = { 1, 4, 2, 6, 3, 7, 5,
			7, };

	static static_codebook _huff_book_line_1024x27_class2 = new static_codebook(
			1, 8, _huff_lengthlist_line_1024x27_class2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_class3 = { 1, 5, 7, 21, 5, 8, 9,
			21, 10, 9, 12, 20, 20, 16, 20, 20, 4, 8, 9, 20, 6, 8, 9, 20, 11,
			11, 13, 20, 20, 15, 17, 20, 9, 11, 14, 20, 8, 10, 15, 20, 11, 13,
			15, 20, 20, 20, 20, 20, 20, 20, 20, 20, 13, 20, 20, 20, 18, 18, 20,
			20, 20, 20, 20, 20, 3, 6, 8, 20, 6, 7, 9, 20, 10, 9, 12, 20, 20,
			20, 20, 20, 5, 7, 9, 20, 6, 6, 9, 20, 10, 9, 12, 20, 20, 20, 20,
			20, 8, 10, 13, 20, 8, 9, 12, 20, 11, 10, 12, 20, 20, 20, 20, 20,
			18, 20, 20, 20, 15, 17, 18, 20, 18, 17, 18, 20, 20, 20, 20, 20, 7,
			10, 12, 20, 8, 9, 11, 20, 14, 13, 14, 20, 20, 20, 20, 20, 6, 9, 12,
			20, 7, 8, 11, 20, 12, 11, 13, 20, 20, 20, 20, 20, 9, 11, 15, 20, 8,
			10, 14, 20, 12, 11, 14, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
			20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 11, 16, 18, 20, 15, 15, 17,
			20, 20, 17, 20, 20, 20, 20, 20, 20, 9, 14, 16, 20, 12, 12, 15, 20,
			17, 15, 18, 20, 20, 20, 20, 20, 16, 19, 18, 20, 15, 16, 20, 20, 17,
			17, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
			20, 20, 20, 20, 20, 20, };

	static static_codebook _huff_book_line_1024x27_class3 = new static_codebook(
			1, 256, _huff_lengthlist_line_1024x27_class3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_class4 = { 2, 3, 7, 13, 4, 4, 7,
			15, 8, 6, 9, 17, 21, 16, 15, 21, 2, 5, 7, 11, 5, 5, 7, 14, 9, 7,
			10, 16, 17, 15, 16, 21, 4, 7, 10, 17, 7, 7, 9, 15, 11, 9, 11, 16,
			21, 18, 15, 21, 18, 21, 21, 21, 15, 17, 17, 19, 21, 19, 18, 20, 21,
			21, 21, 20, };

	static static_codebook _huff_book_line_1024x27_class4 = new static_codebook(
			1, 64, _huff_lengthlist_line_1024x27_class4, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_0sub0 = { 5, 5, 5, 5, 6, 5, 6,
			5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 7, 5, 7, 5, 7,
			5, 7, 5, 8, 6, 8, 6, 8, 6, 9, 6, 9, 6, 10, 6, 10, 6, 11, 6, 11, 7,
			11, 7, 12, 7, 12, 7, 12, 7, 12, 7, 12, 7, 12, 7, 12, 7, 12, 8, 13,
			8, 12, 8, 12, 8, 13, 8, 13, 9, 13, 9, 13, 9, 13, 9, 12, 10, 12, 10,
			13, 10, 14, 11, 14, 12, 14, 13, 14, 13, 14, 14, 15, 16, 15, 15, 15,
			14, 15, 17, 21, 22, 22, 21, 22, 22, 22, 22, 22, 22, 21, 21, 21, 21,
			21, 21, 21, 21, 21, 21, };

	static static_codebook _huff_book_line_1024x27_0sub0 = new static_codebook(
			1, 128, _huff_lengthlist_line_1024x27_0sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_1sub0 = { 2, 5, 5, 4, 5, 4, 5,
			4, 5, 4, 6, 5, 6, 5, 6, 5, 6, 5, 7, 5, 7, 6, 8, 6, 8, 6, 8, 6, 9,
			6, 9, 6, };

	static static_codebook _huff_book_line_1024x27_1sub0 = new static_codebook(
			1, 32, _huff_lengthlist_line_1024x27_1sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_1sub1 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 8, 5, 8, 4, 9, 4, 9, 4, 9, 4, 9, 4, 9, 4, 9, 4, 9, 4, 9,
			4, 9, 4, 8, 4, 8, 4, 9, 5, 9, 5, 9, 5, 9, 5, 9, 6, 10, 6, 10, 7,
			10, 8, 11, 9, 11, 11, 12, 13, 12, 14, 13, 15, 13, 15, 14, 16, 14,
			17, 15, 17, 15, 15, 16, 16, 15, 16, 16, 16, 15, 18, 16, 15, 17, 17,
			19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, };

	static static_codebook _huff_book_line_1024x27_1sub1 = new static_codebook(
			1, 128, _huff_lengthlist_line_1024x27_1sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_2sub0 = { 1, 5, 5, 5, 5, 5, 5,
			5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 6, 7, 7, 7, 7, 8, 7, 8, 8, 9, 8, 10,
			9, 10, 9, };

	static static_codebook _huff_book_line_1024x27_2sub0 = new static_codebook(
			1, 32, _huff_lengthlist_line_1024x27_2sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_2sub1 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 4, 3, 4, 3, 4, 4, 5, 4, 5, 4, 5, 5, 6, 5, 6, 5, 7, 5, 7,
			6, 7, 6, 8, 7, 8, 7, 8, 7, 9, 8, 9, 9, 9, 9, 10, 10, 10, 11, 9, 12,
			9, 12, 9, 15, 10, 14, 9, 13, 10, 13, 10, 12, 10, 12, 10, 13, 10,
			12, 11, 13, 11, 14, 12, 13, 13, 14, 14, 13, 14, 15, 14, 16, 13, 13,
			14, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
			16, 16, 16, 15, 15, };

	static static_codebook _huff_book_line_1024x27_2sub1 = new static_codebook(
			1, 128, _huff_lengthlist_line_1024x27_2sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_3sub1 = { 0, 4, 5, 4, 5, 3, 5,
			3, 5, 3, 5, 4, 4, 4, 4, 5, 5, 5, };

	static static_codebook _huff_book_line_1024x27_3sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_1024x27_3sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_3sub2 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 4, 3, 4, 4, 4, 4, 5, 5, 5,
			5, 5, 6, 5, 7, 5, 8, 6, 8, 6, 9, 7, 10, 7, 10, 8, 10, 8, 11, 9, 11, };

	static static_codebook _huff_book_line_1024x27_3sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_1024x27_3sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_3sub3 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
			7, 3, 8, 3, 10, 3, 8, 3, 9, 3, 8, 4, 9, 4, 9, 5, 9, 6, 10, 6, 9, 7,
			11, 7, 12, 9, 13, 10, 13, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
			12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
			12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
			12, 12, 12, 12, };

	static static_codebook _huff_book_line_1024x27_3sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_1024x27_3sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_4sub1 = { 0, 4, 5, 4, 5, 4, 5,
			4, 5, 3, 5, 3, 5, 3, 5, 4, 5, 4, };

	static static_codebook _huff_book_line_1024x27_4sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_1024x27_4sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_4sub2 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 2, 4, 2, 5, 3, 5, 4, 6, 6, 6,
			7, 7, 8, 7, 8, 7, 8, 7, 9, 8, 9, 8, 9, 8, 10, 8, 11, 9, 12, 9, 12, };

	static static_codebook _huff_book_line_1024x27_4sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_1024x27_4sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_1024x27_4sub3 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
			5, 2, 6, 3, 6, 4, 7, 4, 7, 5, 9, 5, 11, 6, 11, 6, 11, 7, 11, 6, 11,
			6, 11, 9, 11, 8, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
			11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
			11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 10,
			10, 10, 10, 10, 10, };

	static static_codebook _huff_book_line_1024x27_4sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_1024x27_4sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	// _floor_2048x27_books
	static int[] _huff_lengthlist_line_2048x27_class1 = { 2, 6, 8, 9, 7, 11,
			13, 13, 1, 3, 5, 5, 6, 6, 12, 10, };

	static static_codebook _huff_book_line_2048x27_class1 = new static_codebook(
			1, 16, _huff_lengthlist_line_2048x27_class1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_class2 = { 1, 2, 3, 6, 4, 7, 5,
			7, };

	static static_codebook _huff_book_line_2048x27_class2 = new static_codebook(
			1, 8, _huff_lengthlist_line_2048x27_class2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_class3 = { 3, 3, 6, 16, 5, 5, 7,
			16, 9, 8, 11, 16, 16, 16, 16, 16, 5, 5, 8, 16, 5, 5, 7, 16, 8, 7,
			9, 16, 16, 16, 16, 16, 9, 9, 12, 16, 6, 8, 11, 16, 9, 10, 11, 16,
			16, 16, 16, 16, 16, 16, 16, 16, 13, 16, 16, 16, 15, 16, 16, 16, 16,
			16, 16, 16, 5, 4, 7, 16, 6, 5, 8, 16, 9, 8, 10, 16, 16, 16, 16, 16,
			5, 5, 7, 15, 5, 4, 6, 15, 7, 6, 8, 16, 16, 16, 16, 16, 9, 9, 11,
			15, 7, 7, 9, 16, 8, 8, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 15,
			15, 15, 16, 15, 15, 14, 16, 16, 16, 16, 16, 8, 8, 11, 16, 8, 9, 10,
			16, 11, 10, 14, 16, 16, 16, 16, 16, 6, 8, 10, 16, 6, 7, 10, 16, 8,
			8, 11, 16, 14, 16, 16, 16, 10, 11, 14, 16, 9, 9, 11, 16, 10, 10,
			11, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
			16, 16, 16, 16, 16, 16, 16, 16, 16, 15, 16, 16, 16, 16, 16, 16, 16,
			16, 16, 16, 16, 12, 16, 15, 16, 12, 14, 16, 16, 16, 16, 16, 16, 16,
			16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
			16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
			16, };

	static static_codebook _huff_book_line_2048x27_class3 = new static_codebook(
			1, 256, _huff_lengthlist_line_2048x27_class3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_class4 = { 2, 4, 7, 13, 4, 5, 7,
			15, 8, 7, 10, 16, 16, 14, 16, 16, 2, 4, 7, 16, 3, 4, 7, 14, 8, 8,
			10, 16, 16, 16, 15, 16, 6, 8, 11, 16, 7, 7, 9, 16, 11, 9, 13, 16,
			16, 16, 15, 16, 16, 16, 16, 16, 14, 16, 16, 16, 16, 16, 16, 16, 16,
			16, 16, 16, };

	static static_codebook _huff_book_line_2048x27_class4 = new static_codebook(
			1, 64, _huff_lengthlist_line_2048x27_class4, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_0sub0 = { 5, 5, 5, 5, 5, 5, 6,
			5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 7, 5, 7, 5, 7, 5, 8, 5, 8, 5, 8,
			5, 9, 5, 9, 6, 10, 6, 10, 6, 11, 6, 11, 6, 11, 6, 11, 6, 11, 6, 11,
			6, 11, 6, 12, 7, 11, 7, 11, 7, 11, 7, 11, 7, 10, 7, 11, 7, 11, 7,
			12, 7, 11, 8, 11, 8, 11, 8, 11, 8, 13, 8, 12, 9, 11, 9, 11, 9, 11,
			10, 12, 10, 12, 9, 12, 10, 12, 11, 14, 12, 16, 12, 12, 11, 14, 16,
			17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17,
			17, 17, 17, 16, 16, 16, 16, };

	static static_codebook _huff_book_line_2048x27_0sub0 = new static_codebook(
			1, 128, _huff_lengthlist_line_2048x27_0sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_1sub0 = { 4, 4, 4, 4, 4, 4, 4,
			4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 7, 6, 7, 6, 7,
			6, 7, 6, };

	static static_codebook _huff_book_line_2048x27_1sub0 = new static_codebook(
			1, 32, _huff_lengthlist_line_2048x27_1sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_1sub1 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 6, 5, 7, 5, 7, 4, 7, 4, 8, 4, 8, 4, 8, 4, 8, 3, 8, 4, 9,
			4, 9, 4, 9, 4, 9, 4, 9, 5, 9, 5, 9, 6, 9, 7, 9, 8, 9, 9, 9, 10, 9,
			11, 9, 14, 9, 15, 10, 15, 10, 15, 10, 15, 10, 15, 11, 15, 10, 14,
			12, 14, 11, 14, 13, 14, 13, 15, 15, 15, 12, 15, 15, 15, 13, 15, 13,
			15, 13, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
			15, 15, 15, 14, };

	static static_codebook _huff_book_line_2048x27_1sub1 = new static_codebook(
			1, 128, _huff_lengthlist_line_2048x27_1sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_2sub0 = { 2, 4, 5, 4, 5, 4, 5,
			4, 5, 5, 5, 5, 5, 5, 6, 5, 6, 5, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 8,
			8, 8, 8, };

	static static_codebook _huff_book_line_2048x27_2sub0 = new static_codebook(
			1, 32, _huff_lengthlist_line_2048x27_2sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_2sub1 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 3, 4, 3, 4, 3, 4, 4, 5, 4, 5, 5, 5, 6, 6, 6, 7, 6, 8, 6,
			8, 6, 9, 7, 10, 7, 10, 7, 10, 7, 12, 7, 12, 7, 12, 9, 12, 11, 12,
			10, 12, 10, 12, 11, 12, 12, 12, 10, 12, 10, 12, 10, 12, 9, 12, 11,
			12, 12, 12, 12, 12, 11, 12, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12,
			10, 10, 12, 12, 12, 12, 12, 10, 12, 12, 12, 12, 12, 12, 12, 12, 12,
			12, 12, 12, 12, 12, 12, 12, };

	static static_codebook _huff_book_line_2048x27_2sub1 = new static_codebook(
			1, 128, _huff_lengthlist_line_2048x27_2sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_3sub1 = { 0, 4, 4, 4, 4, 4, 4,
			4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, };

	static static_codebook _huff_book_line_2048x27_3sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_2048x27_3sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_3sub2 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5,
			5, 5, 6, 6, 7, 6, 7, 6, 8, 6, 9, 7, 9, 7, 9, 9, 11, 9, 12, 10, 12, };

	static static_codebook _huff_book_line_2048x27_3sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_2048x27_3sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_3sub3 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
			6, 3, 7, 3, 7, 5, 7, 7, 7, 7, 7, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, };

	static static_codebook _huff_book_line_2048x27_3sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_2048x27_3sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_4sub1 = { 0, 3, 4, 4, 4, 4, 4,
			4, 4, 4, 5, 4, 5, 4, 5, 4, 4, 5, };

	static static_codebook _huff_book_line_2048x27_4sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_2048x27_4sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_4sub2 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 4, 3, 4, 4, 4, 5, 5, 6, 5,
			6, 5, 7, 6, 6, 6, 7, 7, 7, 8, 9, 9, 9, 12, 10, 11, 10, 10, 12, 10,
			10, };

	static static_codebook _huff_book_line_2048x27_4sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_2048x27_4sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_2048x27_4sub3 = { 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
			6, 5, 7, 5, 7, 7, 7, 7, 7, 5, 7, 5, 7, 5, 7, 5, 7, 7, 7, 7, 7, 4,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, };

	static static_codebook _huff_book_line_2048x27_4sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_2048x27_4sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	// _floor_512x17_books
	static int[] _huff_lengthlist_line_512x17_0sub0 = { 4, 5, 5, 5, 5, 5, 5, 5,
			5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 5, 6, 6, 6, 6, 5, 6, 6, 7, 6, 7, 6,
			7, 6, 7, 6, 8, 7, 8, 7, 8, 7, 8, 7, 8, 7, 9, 7, 9, 7, 9, 7, 9, 8,
			9, 8, 10, 8, 10, 8, 10, 7, 10, 6, 10, 8, 10, 8, 11, 7, 10, 7, 11,
			8, 11, 11, 12, 12, 11, 11, 12, 11, 13, 11, 13, 11, 13, 12, 15, 12,
			13, 13, 14, 14, 14, 14, 14, 15, 15, 15, 16, 14, 17, 19, 19, 18, 18,
			18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
			18, 18, 18, 18, 18, 18, };

	static static_codebook _huff_book_line_512x17_0sub0 = new static_codebook(
			1, 128, _huff_lengthlist_line_512x17_0sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_1sub0 = { 2, 4, 5, 4, 5, 4, 5, 4,
			5, 5, 5, 5, 5, 5, 6, 5, 6, 5, 6, 6, 7, 6, 7, 6, 8, 7, 8, 7, 8, 7,
			8, 7, };

	static static_codebook _huff_book_line_512x17_1sub0 = new static_codebook(
			1, 32, _huff_lengthlist_line_512x17_1sub0, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_1sub1 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 4, 3, 5, 3, 5, 4, 5, 4, 5, 4, 5, 5, 5, 5, 6, 5, 6, 5, 7, 5,
			8, 6, 8, 6, 8, 6, 8, 6, 8, 7, 9, 7, 9, 7, 11, 9, 11, 11, 12, 11,
			14, 12, 14, 16, 14, 16, 13, 16, 14, 16, 12, 15, 13, 16, 14, 16, 13,
			14, 12, 15, 13, 15, 13, 13, 13, 15, 12, 14, 14, 15, 13, 15, 12, 15,
			15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
			15, 15, 15, 15, 15, };

	static static_codebook _huff_book_line_512x17_1sub1 = new static_codebook(
			1, 128, _huff_lengthlist_line_512x17_1sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_2sub1 = { 0, 4, 5, 4, 4, 4, 5, 4,
			4, 4, 5, 4, 5, 4, 5, 3, 5, 3, };

	static static_codebook _huff_book_line_512x17_2sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_512x17_2sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_2sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 3, 4, 3, 4, 4, 5, 4, 5, 4, 6, 4,
			6, 5, 6, 5, 7, 5, 7, 6, 8, 6, 8, 6, 8, 7, 8, 7, 9, 7, 9, 8, };

	static static_codebook _huff_book_line_512x17_2sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_512x17_2sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_2sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3,
			3, 3, 4, 3, 4, 4, 5, 5, 6, 6, 7, 7, 7, 8, 8, 11, 8, 9, 9, 9, 10,
			11, 11, 11, 9, 10, 10, 11, 11, 11, 11, 10, 10, 10, 10, 10, 10, 10,
			10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
			10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
			10, 10, 10, 10, };

	static static_codebook _huff_book_line_512x17_2sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_512x17_2sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_3sub1 = { 0, 4, 4, 4, 4, 4, 4, 3,
			4, 4, 4, 4, 4, 5, 4, 5, 5, 5, };

	static static_codebook _huff_book_line_512x17_3sub1 = new static_codebook(
			1, 18, _huff_lengthlist_line_512x17_3sub1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_3sub2 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 4, 3, 5, 4, 6, 4, 6, 5, 7,
			6, 7, 6, 8, 6, 8, 7, 9, 8, 10, 8, 12, 9, 13, 10, 15, 10, 15, 11,
			14, };

	static static_codebook _huff_book_line_512x17_3sub2 = new static_codebook(
			1, 50, _huff_lengthlist_line_512x17_3sub2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_3sub3 = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 8,
			4, 8, 4, 8, 4, 8, 5, 8, 5, 8, 6, 8, 4, 8, 4, 8, 5, 8, 5, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, };

	static static_codebook _huff_book_line_512x17_3sub3 = new static_codebook(
			1, 128, _huff_lengthlist_line_512x17_3sub3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_class1 = { 1, 2, 3, 6, 5, 4, 7,
			7, };

	static static_codebook _huff_book_line_512x17_class1 = new static_codebook(
			1, 8, _huff_lengthlist_line_512x17_class1, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_class2 = { 3, 3, 3, 14, 5, 4, 4,
			11, 8, 6, 6, 10, 17, 12, 11, 17, 6, 5, 5, 15, 5, 3, 4, 11, 8, 5, 5,
			8, 16, 9, 10, 14, 10, 8, 9, 17, 8, 6, 6, 13, 10, 7, 7, 10, 16, 11,
			13, 14, 17, 17, 17, 17, 17, 16, 16, 16, 16, 15, 16, 16, 16, 16, 16,
			16, };

	static static_codebook _huff_book_line_512x17_class2 = new static_codebook(
			1, 64, _huff_lengthlist_line_512x17_class2, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static int[] _huff_lengthlist_line_512x17_class3 = { 2, 4, 6, 17, 4, 5, 7,
			17, 8, 7, 10, 17, 17, 17, 17, 17, 3, 4, 6, 15, 3, 3, 6, 15, 7, 6,
			9, 17, 17, 17, 17, 17, 6, 8, 10, 17, 6, 6, 8, 16, 9, 8, 10, 17, 17,
			15, 16, 17, 17, 17, 17, 17, 12, 15, 15, 16, 12, 15, 15, 16, 16, 16,
			16, 16, };

	static static_codebook _huff_book_line_512x17_class3 = new static_codebook(
			1, 64, _huff_lengthlist_line_512x17_class3, 0, 0, 0, 0, 0, null,
			null, null, null, 0);

	static static_codebook[] _floor_128x4_books = {
			_huff_book_line_128x4_class0, _huff_book_line_128x4_0sub0,
			_huff_book_line_128x4_0sub1, _huff_book_line_128x4_0sub2,
			_huff_book_line_128x4_0sub3, };

	static static_codebook[] _floor_256x4_books = {
			_huff_book_line_256x4_class0, _huff_book_line_256x4_0sub0,
			_huff_book_line_256x4_0sub1, _huff_book_line_256x4_0sub2,
			_huff_book_line_256x4_0sub3, };

	static static_codebook[] _floor_128x7_books = {
			_huff_book_line_128x7_class0, _huff_book_line_128x7_class1,

			_huff_book_line_128x7_0sub1, _huff_book_line_128x7_0sub2,
			_huff_book_line_128x7_0sub3, _huff_book_line_128x7_1sub1,
			_huff_book_line_128x7_1sub2, _huff_book_line_128x7_1sub3, };

	static static_codebook[] _floor_256x7_books = {
			_huff_book_line_256x7_class0, _huff_book_line_256x7_class1,

			_huff_book_line_256x7_0sub1, _huff_book_line_256x7_0sub2,
			_huff_book_line_256x7_0sub3, _huff_book_line_256x7_1sub1,
			_huff_book_line_256x7_1sub2, _huff_book_line_256x7_1sub3, };

	static static_codebook[] _floor_128x11_books = {
			_huff_book_line_128x11_class1, _huff_book_line_128x11_class2,
			_huff_book_line_128x11_class3,

			_huff_book_line_128x11_0sub0, _huff_book_line_128x11_1sub0,
			_huff_book_line_128x11_1sub1, _huff_book_line_128x11_2sub1,
			_huff_book_line_128x11_2sub2, _huff_book_line_128x11_2sub3,
			_huff_book_line_128x11_3sub1, _huff_book_line_128x11_3sub2,
			_huff_book_line_128x11_3sub3, };

	static static_codebook[] _floor_128x17_books = {
			_huff_book_line_128x17_class1, _huff_book_line_128x17_class2,
			_huff_book_line_128x17_class3,

			_huff_book_line_128x17_0sub0, _huff_book_line_128x17_1sub0,
			_huff_book_line_128x17_1sub1, _huff_book_line_128x17_2sub1,
			_huff_book_line_128x17_2sub2, _huff_book_line_128x17_2sub3,
			_huff_book_line_128x17_3sub1, _huff_book_line_128x17_3sub2,
			_huff_book_line_128x17_3sub3, };

	static static_codebook[] _floor_256x4low_books = {
			_huff_book_line_256x4low_class0, _huff_book_line_256x4low_0sub0,
			_huff_book_line_256x4low_0sub1, _huff_book_line_256x4low_0sub2,
			_huff_book_line_256x4low_0sub3, };

	static static_codebook[] _floor_1024x27_books = {
			_huff_book_line_1024x27_class1, _huff_book_line_1024x27_class2,
			_huff_book_line_1024x27_class3, _huff_book_line_1024x27_class4,

			_huff_book_line_1024x27_0sub0, _huff_book_line_1024x27_1sub0,
			_huff_book_line_1024x27_1sub1, _huff_book_line_1024x27_2sub0,
			_huff_book_line_1024x27_2sub1, _huff_book_line_1024x27_3sub1,
			_huff_book_line_1024x27_3sub2, _huff_book_line_1024x27_3sub3,
			_huff_book_line_1024x27_4sub1, _huff_book_line_1024x27_4sub2,
			_huff_book_line_1024x27_4sub3, };

	static static_codebook[] _floor_2048x27_books = {
			_huff_book_line_2048x27_class1, _huff_book_line_2048x27_class2,
			_huff_book_line_2048x27_class3, _huff_book_line_2048x27_class4,

			_huff_book_line_2048x27_0sub0, _huff_book_line_2048x27_1sub0,
			_huff_book_line_2048x27_1sub1, _huff_book_line_2048x27_2sub0,
			_huff_book_line_2048x27_2sub1, _huff_book_line_2048x27_3sub1,
			_huff_book_line_2048x27_3sub2, _huff_book_line_2048x27_3sub3,
			_huff_book_line_2048x27_4sub1, _huff_book_line_2048x27_4sub2,
			_huff_book_line_2048x27_4sub3, };

	static static_codebook[] _floor_512x17_books = {
			_huff_book_line_512x17_class1, _huff_book_line_512x17_class2,
			_huff_book_line_512x17_class3,

			_huff_book_line_512x17_0sub0, _huff_book_line_512x17_1sub0,
			_huff_book_line_512x17_1sub1, _huff_book_line_512x17_2sub1,
			_huff_book_line_512x17_2sub2, _huff_book_line_512x17_2sub3,
			_huff_book_line_512x17_3sub1, _huff_book_line_512x17_3sub2,
			_huff_book_line_512x17_3sub3, };

	public static_codebook[][] _floor_books = { _floor_128x4_books,
			_floor_256x4_books, _floor_128x7_books, _floor_256x7_books,
			_floor_128x11_books, _floor_128x17_books, _floor_256x4low_books,
			_floor_1024x27_books, _floor_2048x27_books, _floor_512x17_books, };

	// TODO - class variable initialization
	// readup on the best way to do this for java
	// maybe read from external file or project resource
	// to cut down on the .class size footprint

	public floor_books() {
	}

}
