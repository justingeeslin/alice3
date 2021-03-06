/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.story.resources.sims2;

import edu.cmu.cs.dennisc.random.RandomUtilities;

import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public enum BaseSkinTone implements SkinTone {
  LIGHTER(new Color(206, 148, 115)), LIGHT(new Color(189, 129, 90)), DARK(new Color(158, 102, 58)), DARKER(new Color(102, 54, 13));
  private final Color color;

  private BaseSkinTone(Color color) {
    this.color = color;
  }

  public static BaseSkinTone getRandom() {
    return RandomUtilities.getRandomEnumConstant(BaseSkinTone.class);
  }

  //todo: package-private
  public Color getColor() {
    return this.color;
  }

  //todo: package-private
  public static BaseSkinTone getClosestToColor(Color other) {
    if (other != null) {
      float[] hsbOther = new float[3];
      float[] hsb = new float[3];

      Color.RGBtoHSB(other.getRed(), other.getGreen(), other.getBlue(), hsbOther);

      BaseSkinTone minBaseSkinTone = null;
      float minDistanceSquared = Float.MAX_VALUE;
      for (BaseSkinTone baseSkinTone : values()) {
        Color color = baseSkinTone.getColor();
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        //note: ignore hue for now
        float sDelta = hsbOther[1] - hsb[1];
        float bDelta = hsbOther[2] - hsb[2];
        float distanceSquared = (sDelta * sDelta) + (bDelta * bDelta);
        if (distanceSquared < minDistanceSquared) {
          minBaseSkinTone = baseSkinTone;
          minDistanceSquared = distanceSquared;
        }
      }
      return minBaseSkinTone;
    } else {
      return null;
    }
  }
}
