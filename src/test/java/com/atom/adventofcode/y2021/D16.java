package com.atom.adventofcode.y2021;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 16: Packet Decoder ---
 *
 * As you leave the cave and reach open waters, you receive a transmission from the Elves back on the ship.
 *
 * The transmission was sent using the Buoyancy Interchange Transmission System (BITS), a method of packing numeric
 * expressions into a binary sequence. Your submarine's computer has saved the transmission in hexadecimal (your puzzle
 * input).
 *
 * The first step of decoding the message is to convert the hexadecimal representation into binary. Each character of
 * hexadecimal corresponds to four bits of binary data:
 *
 * 0 = 0000
 * 1 = 0001
 * 2 = 0010
 * 3 = 0011
 * 4 = 0100
 * 5 = 0101
 * 6 = 0110
 * 7 = 0111
 * 8 = 1000
 * 9 = 1001
 * A = 1010
 * B = 1011
 * C = 1100
 * D = 1101
 * E = 1110
 * F = 1111
 *
 * The BITS transmission contains a single packet at its outermost layer which itself contains many other packets. The
 * hexadecimal representation of this packet might encode a few extra 0 bits at the end; these are not part of the
 * transmission and should be ignored.
 *
 * Every packet begins with a standard header: the first three bits encode the packet version, and the next three bits
 * encode the packet type ID. These two values are numbers; all numbers encoded in any packet are represented as binary
 * with the most significant bit first. For example, a version encoded as the binary sequence 100 represents the number
 * 4.
 *
 * Packets with type ID 4 represent a literal value. Literal value packets encode a single binary number. To do this,
 * the binary number is padded with leading zeroes until its length is a multiple of four bits, and then it is broken
 * into groups of four bits. Each group is prefixed by a 1 bit except the last group, which is prefixed by a 0 bit.
 * These groups of five bits immediately follow the packet header. For example, the hexadecimal string D2FE28 becomes:
 *
 * 110100101111111000101000
 * VVVTTTAAAAABBBBBCCCCC
 *
 * Below each bit is a label indicating its purpose:
 *
 *     The three bits labeled V (110) are the packet version, 6.
 *     The three bits labeled T (100) are the packet type ID, 4, which means the packet is a literal value.
 *     The five bits labeled A (10111) start with a 1 (not the last group, keep reading) and contain the first four
 *     bits of the number, 0111.
 *     The five bits labeled B (11110) start with a 1 (not the last group, keep reading) and contain four more bits of
 *     the number, 1110.
 *     The five bits labeled C (00101) start with a 0 (last group, end of packet) and contain the last four bits of the
 *     number, 0101.
 *     The three unlabeled 0 bits at the end are extra due to the hexadecimal representation and should be ignored.
 *
 * So, this packet represents a literal value with binary representation 011111100101, which is 2021 in decimal.
 *
 * Every other type of packet (any packet with a type ID other than 4) represent an operator that performs some
 * calculation on one or more sub-packets contained within. Right now, the specific operations aren't important; focus
 * on parsing the hierarchy of sub-packets.
 *
 * An operator packet contains one or more packets. To indicate which subsequent binary data represents its sub-packets,
 * an operator packet can use one of two modes indicated by the bit immediately after the packet header; this is called
 * the length type ID:
 *
 *     If the length type ID is 0, then the next 15 bits are a number that represents the total length in bits of the
 *     sub-packets contained by this packet.
 *     If the length type ID is 1, then the next 11 bits are a number that represents the number of sub-packets
 *     immediately contained by this packet.
 *
 * Finally, after the length type ID bit and the 15-bit or 11-bit field, the sub-packets appear.
 *
 * For example, here is an operator packet (hexadecimal string 38006F45291200) with length type ID 0 that contains two
 * sub-packets:
 *
 * 00111000000000000110111101000101001010010001001000000000
 * VVVTTTILLLLLLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBBBBBB
 *
 *     The three bits labeled V (001) are the packet version, 1.
 *     The three bits labeled T (110) are the packet type ID, 6, which means the packet is an operator.
 *     The bit labeled I (0) is the length type ID, which indicates that the length is a 15-bit number representing the
 *     number of bits in the sub-packets.
 *     The 15 bits labeled L (000000000011011) contain the length of the sub-packets in bits, 27.
 *     The 11 bits labeled A contain the first sub-packet, a literal value representing the number 10.
 *     The 16 bits labeled B contain the second sub-packet, a literal value representing the number 20.
 *
 * After reading 11 and 16 bits of sub-packet data, the total length indicated in L (27) is reached, and so parsing of
 * this packet stops.
 *
 * As another example, here is an operator packet (hexadecimal string EE00D40C823060) with length type ID 1 that
 * contains three sub-packets:
 *
 * 11101110000000001101010000001100100000100011000001100000
 * VVVTTTILLLLLLLLLLLAAAAAAAAAAABBBBBBBBBBBCCCCCCCCCCC
 *
 *     The three bits labeled V (111) are the packet version, 7.
 *     The three bits labeled T (011) are the packet type ID, 3, which means the packet is an operator.
 *     The bit labeled I (1) is the length type ID, which indicates that the length is a 11-bit number representing the
 *     number of sub-packets.
 *     The 11 bits labeled L (00000000011) contain the number of sub-packets, 3.
 *     The 11 bits labeled A contain the first sub-packet, a literal value representing the number 1.
 *     The 11 bits labeled B contain the second sub-packet, a literal value representing the number 2.
 *     The 11 bits labeled C contain the third sub-packet, a literal value representing the number 3.
 *
 * After reading 3 complete sub-packets, the number of sub-packets indicated in L (3) is reached, and so parsing of this packet stops.
 *
 * For now, parse the hierarchy of the packets throughout the transmission and add up all of the version numbers.
 *
 * Here are a few more examples of hexadecimal-encoded transmissions:
 *
 *     8A004A801A8002F478 represents an operator packet (version 4) which contains an operator packet (version 1) which
 *     contains an operator packet (version 5) which contains a literal value (version 6); this packet has a version
 *     sum of 16.
 *     620080001611562C8802118E34 represents an operator packet (version 3) which contains two sub-packets; each
 *     sub-packet is an operator packet that contains two literal values. This packet has a version sum of 12.
 *     C0015000016115A2E0802F182340 has the same structure as the previous example, but the outermost packet uses a
 *     different length type ID. This packet has a version sum of 23.
 *     A0016C880162017C3686B18A3D4780 is an operator packet that contains an operator packet that contains an operator
 *     packet that contains five literal values; it has a version sum of 31.
 *
 * Decode the structure of your hexadecimal-encoded BITS transmission; what do you get if you add up the version numbers
 * in all packets?
 *
 * Your puzzle answer was 920.
 *
 * The first half of this puzzle is complete! It provides one gold star: *
 * --- Part Two ---
 *
 * Now that you have the structure of your transmission decoded, you can calculate the value of the expression it represents.
 *
 * Literal values (type ID 4) represent a single number as described above. The remaining type IDs are more interesting:
 *
 *     Packets with type ID 0 are sum packets - their value is the sum of the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
 *     Packets with type ID 1 are product packets - their value is the result of multiplying together the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
 *     Packets with type ID 2 are minimum packets - their value is the minimum of the values of their sub-packets.
 *     Packets with type ID 3 are maximum packets - their value is the maximum of the values of their sub-packets.
 *     Packets with type ID 5 are greater than packets - their value is 1 if the value of the first sub-packet is greater than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
 *     Packets with type ID 6 are less than packets - their value is 1 if the value of the first sub-packet is less than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
 *     Packets with type ID 7 are equal to packets - their value is 1 if the value of the first sub-packet is equal to the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
 *
 * Using these rules, you can now work out the value of the outermost packet in your BITS transmission.
 *
 * For example:
 *
 *     C200B40A82 finds the sum of 1 and 2, resulting in the value 3.
 *     04005AC33890 finds the product of 6 and 9, resulting in the value 54.
 *     880086C3E88112 finds the minimum of 7, 8, and 9, resulting in the value 7.
 *     CE00C43D881120 finds the maximum of 7, 8, and 9, resulting in the value 9.
 *     D8005AC2A8F0 produces 1, because 5 is less than 15.
 *     F600BC2D8F produces 0, because 5 is not greater than 15.
 *     9C005AC2F8F0 produces 0, because 5 is not equal to 15.
 *     9C0141080250320F1802104A08 produces 1, because 1 + 3 = 2 * 2.
 *
 * What do you get if you evaluate the expression represented by your hexadecimal-encoded BITS transmission?
 */
public class D16 {

    public static final String puzzleInput = "E20D72805F354AE298E2FCC5339218F90FE5F3A388BA60095005C3352CF7FBF27CD4B3DFEFC95354723006C401C8FD1A23280021D1763CC791006E25C198A6C01254BAECDED7A5A99CCD30C01499CFB948F857002BB9FCD68B3296AF23DD6BE4C600A4D3ED006AA200C4128E10FC0010C8A90462442A5006A7EB2429F8C502675D13700BE37CF623EB3449CAE732249279EFDED801E898A47BE8D23FBAC0805527F99849C57A5270C064C3ECF577F4940016A269007D3299D34E004DF298EC71ACE8DA7B77371003A76531F20020E5C4CC01192B3FE80293B7CD23ED55AA76F9A47DAAB6900503367D240522313ACB26B8801B64CDB1FB683A6E50E0049BE4F6588804459984E98F28D80253798DFDAF4FE712D679816401594EAA580232B19F20D92E7F3740D1003880C1B002DA1400B6028BD400F0023A9C00F50035C00C5002CC0096015B0C00B30025400D000C398025E2006BD800FC9197767C4026D78022000874298850C4401884F0E21EC9D256592007A2C013967C967B8C32BCBD558C013E005F27F53EB1CE25447700967EBB2D95BFAE8135A229AE4FFBB7F6BC6009D006A2200FC3387D128001088E91121F4DED58C025952E92549C3792730013ACC0198D709E349002171060DC613006E14C7789E4006C4139B7194609DE63FEEB78004DF299AD086777ECF2F311200FB7802919FACB38BAFCFD659C5D6E5766C40244E8024200EC618E11780010B83B09E1BCFC488C017E0036A184D0A4BB5CDD0127351F56F12530046C01784B3FF9C6DFB964EE793F5A703360055A4F71F12C70000EC67E74ED65DE44AA7338FC275649D7D40041E4DDA794C80265D00525D2E5D3E6F3F26300426B89D40094CCB448C8F0C017C00CC0401E82D1023E0803719E2342D9FB4E5A01300665C6A5502457C8037A93C63F6B4C8B40129DF7AC353EF2401CC6003932919B1CEE3F1089AB763D4B986E1008A7354936413916B9B080";
    public static final Map<Character, String> m;

    static {
        m = new HashMap<>();
        m.put('0', "0000");
        m.put('1', "0001");
        m.put('2', "0010");
        m.put('3', "0011");
        m.put('4', "0100");
        m.put('5', "0101");
        m.put('6', "0110");
        m.put('7', "0111");
        m.put('8', "1000");
        m.put('9', "1001");
        m.put('A', "1010");
        m.put('B', "1011");
        m.put('C', "1100");
        m.put('D', "1101");
        m.put('E', "1110");
        m.put('F', "1111");
    }

    public String hexToString(String data) {
        StringBuilder sb = new StringBuilder();
        for (Character c : data.toCharArray()) {
            sb.append(m.get(c));
        }
        return sb.toString();
    }

    public int stringToDecimal(String s) {
        return s.chars().reduce(0, (r, c) -> {
            r = r << 1;
            if (c == '1') r++;
            return r;
        });
    }

    class Packet {
        int version, id;
        long num;
        List<Packet> subPackets = new ArrayList<>();

        @Override
        public String toString() {
            return "Packet{" +
                    "version=" + version +
                    ", id=" + id +
                    ", num=" + num +
                    ", subPackets=" + subPackets +
                    '}';
        }
    }

    public int parsePacketString(String pkt, int idx, Packet p) {
        p.version = stringToDecimal(pkt.substring(idx, idx + 3));
        p.id = stringToDecimal(pkt.substring(idx + 3, idx + 6));
        idx += 6;

        if (p.id == 4) {
            List<String> nums = new ArrayList<>();
            while (true) {
                nums.add(pkt.substring(idx, idx + 5));
                if (pkt.charAt(idx) == '0') {
                    idx += 5;
                    break;
                }
                idx += 5;
            }
            // Convert to String
            StringBuilder ss = new StringBuilder();
            for (String n : nums) {
                ss.append(n.substring(1));
            }
            p.num = stringToDecimal(ss.toString());
        } else {
            String i = pkt.substring(idx, ++idx);
            if (i.equals("1")) {
                int numPkts = stringToDecimal(pkt.substring(idx, idx + 11));
                idx += 11;
                for (int n = 0; n < numPkts; n++) {
                    Packet pp = new Packet();
                    p.subPackets.add(pp);
                    idx = parsePacketString(pkt, idx, pp);
                }
            } else {
                int len = stringToDecimal(pkt.substring(idx, idx + 15));
                idx += 15;
                int start = idx;

                while (idx - start < len) {
                    Packet pp = new Packet();
                    p.subPackets.add(pp);
                    idx = parsePacketString(pkt, idx, pp);
                }
            }
        }

        return idx;
    }

    public int sumVersionNumbers(Packet pkt) {
        int sum = pkt.version;
        for (Packet p : pkt.subPackets) {
            sum += sumVersionNumbers(p);
        }
        return sum;
    }

    @Test
    public void testPart1() {
        Packet pkt;
        assertEquals("110100101111111000101000", hexToString("D2FE28"));
        pkt = parsePacket("D2FE28");
        assertEquals(2021, pkt.num);

        assertEquals("00111000000000000110111101000101001010010001001000000000", hexToString("38006F45291200"));
        pkt = parsePacket("38006F45291200");
        System.out.println(pkt);

        pkt = parsePacket("EE00D40C823060");
        System.out.println(pkt);

        pkt = parsePacket(puzzleInput);
        System.out.println(pkt);
        assertEquals(920, sumVersionNumbers(pkt));
    }

    static public long evaluatePacket(Packet pkt) {
        switch(pkt.id) {
            case 0 -> {
                return pkt.subPackets.stream().map(D16::evaluatePacket).reduce(0L, Long::sum);
            }
            case 1 -> {
                return pkt.subPackets.stream().map(D16::evaluatePacket).reduce(1L, (res, i) -> res * i);
            }
            case 2 -> {
                return pkt.subPackets.stream().map(D16::evaluatePacket).mapToLong(i->i).min().orElseThrow();
            }
            case 3 -> {
                return pkt.subPackets.stream().map(D16::evaluatePacket).mapToLong(i->i).max().orElseThrow();
            }
            case 4 -> {
                return pkt.num;
            }
            case 5 -> {
                return pkt.subPackets.get(0).num > pkt.subPackets.get(1).num ? 1 : 0;
            }
            case 6 -> {
                return pkt.subPackets.get(0).num < pkt.subPackets.get(1).num ? 1 : 0;
            }
            case 7 -> {
                return pkt.subPackets.get(0).num == pkt.subPackets.get(1).num ? 1 : 0;
            }
        }
        return 1;
    }

    public Packet parsePacket(String strP) {
        Packet pkt = new Packet();
        parsePacketString(hexToString(strP), 0, pkt);
        return pkt;
    }


    @Test
    public void testPart3() {
        long res = Arrays.stream(new long[] {2, 2, 2, 2}).reduce(1L, (r, i) -> r * i);
        System.out.println(res);
    }

    @Test
    public void testPart2() {
        assertEquals(3, evaluatePacket(parsePacket("C200B40A82")));
        assertEquals(54, evaluatePacket(parsePacket("04005AC33890")));
        assertEquals(7, evaluatePacket(parsePacket("880086C3E88112")));
        assertEquals(9, evaluatePacket(parsePacket("CE00C43D881120")));
        assertEquals(1, evaluatePacket(parsePacket("D8005AC2A8F0")));
        assertEquals(0, evaluatePacket(parsePacket("F600BC2D8F")));
        assertEquals(0, evaluatePacket(parsePacket("9C005AC2F8F0")));
        assertEquals(1, evaluatePacket(parsePacket("9C0141080250320F1802104A08")));

        assertEquals(0, evaluatePacket(parsePacket(puzzleInput)));

    }
}