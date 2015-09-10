
package com.allegion.androidtesttools.BLeUtility;


import android.util.Log;

import com.allegion.androidtesttools.BLeUtility.Advertisement;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.UUID;

/**
 * Workaround for android <a href=
 * "https://code.google.com/p/android/issues/detail?id=59490&q=BLE&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars"
 * >Bug #59490</a>
 *
 * @param reversedAdvertisedData The advertised data
 * @return List<UUID> The list of uuid contained in the advertised data.
 */

public class AdvertisementParser
{
    public final static String LOG_TAG = AdvertisementParser.class.getSimpleName();

    public static Advertisement parseUUIDs(final byte[] advertisedData)
    {
//		Log.d(LOG_TAG, "advert data: " + bytesToHex(advertisedData));
        Advertisement advertisement = new Advertisement();

        int offset = 0;
        while (offset < (advertisedData.length - 2))
        {
            int length = advertisedData[offset++];

            if (length == 0)
                break;

            int type = advertisedData[offset++];

//			Log.d(LOG_TAG, "type: " + type);

            switch (type)
            {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (length > 1)
                    {
                        int uuid16 = advertisedData[offset++];
                        uuid16 += (advertisedData[offset++] << 8);
                        length -= 2;
                        advertisement.addUUID(UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid16)));
                    }
                    break;
                case 0x06:// Partial list of 128-bit UUIDs
                case 0x07:// Company information
                    // Loop through the advertised 128-bit UUID's.
                    while (length >= 16)
                    {
                        try
                        {
                            // Wrap the advertised bits and order them.
                            ByteBuffer buffer = ByteBuffer.wrap(advertisedData, offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
                            long mostSignificantBit = buffer.getLong();
                            long leastSignificantBit = buffer.getLong();
                            advertisement.addUUID(new UUID(leastSignificantBit, mostSignificantBit));
                        }
                        catch (IndexOutOfBoundsException e)
                        {
                            // Defensive programming.
                            Log.e(LOG_TAG, e.toString());
                            continue;
                        }
                        finally
                        {
                            // Move the offset to read the next uuid.
                            offset += 15;
                            length -= 16;
                        }
                    }
                    break;

                case -0x01:// Company information

//				Log.d(LOG_TAG, "Advert company info: " + bytesToHex(Arrays.copyOfRange(advertisedData, offset, offset + length - 1)));
                    advertisement.setCompanyData(Arrays.copyOfRange(advertisedData, offset, offset + length - 1));

                    offset += (length - 1);
                    break;

                case 22:

                    length -= 2;
                    offset += 2;
//				Log.d(LOG_TAG, bytesToHex(Arrays.copyOfRange(advertisedData, offset, offset + length -1)));

                    advertisement.setSerialNumber(Arrays.copyOfRange(advertisedData, offset , offset + length -1));

                    offset += (length - 1);
                    break;

                default:
                    offset += (length - 1);
                    break;
            }
        }

        return advertisement;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];

        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}
