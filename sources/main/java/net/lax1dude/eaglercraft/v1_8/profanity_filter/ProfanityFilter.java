/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.v1_8.profanity_filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentScore;
import net.minecraft.util.ChatComponentSelector;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class ProfanityFilter {

	private static final Logger logger = LogManager.getLogger("ProfanityFilter");

	protected final Set<String>[] bannedWords;

	private static ProfanityFilter instance = null;

	public static ProfanityFilter getInstance() {
		if(instance == null) {
			instance = new ProfanityFilter();
		}
		return instance;
	}

	private ProfanityFilter() {
		logger.info("Loading profanity filter hash set...");
		List<String> strs = EagRuntime.getResourceLines("profanity_filter.wlist");
		if(strs == null) {
			throw new RuntimeException("File is missing: profanity_filter.wlist");
		}
		long start = EagRuntime.steadyTimeMillis();
		Set<String>[] sets = new Set[0];
		int j = 0;
		for(String str : strs) {
			if(nextDelimiter(str, 0) != -1) continue;
			str = LookAlikeUnicodeConv.convertString(str);
			int l = str.length();
			if(l == 0) continue;
			if(sets.length < l) {
				Set<String>[] setsNew = new Set[l];
				System.arraycopy(sets, 0, setsNew, 0, sets.length);
				sets = setsNew;
			}
			--l;
			if(sets[l] == null) {
				sets[l] = new HashSet();
			}
			if(sets[l].add(str)) {
				++j;
			}
		}
		bannedWords = sets;
		logger.info("Processed {} entries after {}ms", j, (EagRuntime.steadyTimeMillis() - start));
	}

	public IChatComponent profanityFilterChatComponent(IChatComponent componentIn) {
		if(componentIn == null) return null;
		IChatComponent cmp = null;
		//long start = System.nanoTime();
		try {
			cmp = profanityFilterChatComponent0(componentIn);
		}catch(Throwable t) {
			logger.error("Profanity filter raised an exception on chat component!");
			logger.error(t);
		}
		//logger.info("Took: {}", ((System.nanoTime() - start) / 1000000.0));
		return cmp != null ? cmp : componentIn;
	}

	protected IChatComponent profanityFilterChatComponent0(IChatComponent componentIn) {
		if(componentIn instanceof ChatComponentStyle) {
			boolean flag = false;
			if(componentIn instanceof ChatComponentText) {
				ChatComponentText comp = (ChatComponentText)componentIn;
				String str = comp.getChatComponentText_TextValue();
				if(StringUtils.isEmpty(str) && componentIn.getSiblings().isEmpty()) {
					return componentIn;
				}
				str = profanityFilterString0(str);
				if(str != null) {
					IChatComponent replacedComponent = new ChatComponentText(str);
					replacedComponent.setChatStyle(comp.getChatStyleIfPresent());
					replacedComponent.getSiblings().addAll(componentIn.getSiblings());
					componentIn = replacedComponent;
					flag = true;
				}
			}else if(componentIn instanceof ChatComponentTranslation) {
				ChatComponentTranslation comp = (ChatComponentTranslation)componentIn;
				IChatComponent replacedComponent = new ChatComponentTranslation(comp.getKey(), profanityFilterFormatArgs(comp.getFormatArgs()));
				replacedComponent.setChatStyle(comp.getChatStyleIfPresent());
				replacedComponent.getSiblings().addAll(componentIn.getSiblings());
				componentIn = replacedComponent;
				flag = true;
			}
			List<IChatComponent> siblings = componentIn.getSiblings();
			for(int i = 0, l = siblings.size(); i < l; ++i) {
				IChatComponent cmp = profanityFilterChatComponent0(siblings.get(i));
				if(cmp != null) {
					if(!flag) {
						componentIn = shallowCopy(componentIn);
						siblings = componentIn.getSiblings();
						flag = true;
					}
					siblings.set(i, cmp);
				}
			}
			ChatStyle styleOpt = ((ChatComponentStyle)componentIn).getChatStyleIfPresent();
			if(styleOpt != null) {
				HoverEvent hoverEvent = styleOpt.getChatHoverEvent();
				if(hoverEvent != null) {
					HoverEvent filteredHoverEvent = profanityFilterHoverEvent(hoverEvent);
					if(filteredHoverEvent != null) {
						if(!flag) {
							componentIn = shallowCopy(componentIn);
							flag = true;
						}
						ChatStyle newStyle = styleOpt.createShallowCopy();
						newStyle.setChatHoverEvent(filteredHoverEvent);
						componentIn.setChatStyle(newStyle);
					}
				}
			}
			return flag ? componentIn : null;
		}else {
			return null;
		}
	}

	private Object[] profanityFilterFormatArgs(Object[] formatArgs) {
		Object[] ret = profanityFilterFormatArgs0(formatArgs);
		return ret != null ? ret : formatArgs;
	}

	private Object[] profanityFilterFormatArgs0(Object[] formatArgs) {
		Object[] ret = null;
		for(int i = 0; i < formatArgs.length; ++i) {
			if(formatArgs[i] != null) {
				String arg = formatArgs[i].toString();
				arg = profanityFilterString0(arg);
				if(arg != null) {
					if(ret == null) {
						ret = new Object[formatArgs.length];
						System.arraycopy(formatArgs, 0, ret, 0, ret.length);
					}
					ret[i] = arg;
				}
			}
			
		}
		return ret;
	}

	protected HoverEvent profanityFilterHoverEvent(HoverEvent evtIn) {
		if(evtIn.getAction() == HoverEvent.Action.SHOW_TEXT) {
			IChatComponent filtered = evtIn.getValue();
			if(filtered != null) {
				filtered = profanityFilterChatComponent0(filtered);
				if(filtered != null) {
					return new HoverEvent(evtIn.getAction(), filtered);
				}
			}
		}
		return null;
	}

	private static IChatComponent shallowCopy(IChatComponent comp) {
		if(comp instanceof ChatComponentStyle) {
			if(comp instanceof ChatComponentText) {
				ChatComponentText old = (ChatComponentText)comp;
				IChatComponent ret = new ChatComponentText(old.getChatComponentText_TextValue());
				ret.setChatStyle(old.getChatStyleIfPresent());
				ret.getSiblings().addAll(comp.getSiblings());
				return ret;
			}else if(comp instanceof ChatComponentTranslation) {
				ChatComponentTranslation old = (ChatComponentTranslation)comp;
				IChatComponent ret = new ChatComponentTranslation(old.getKey(), old.getFormatArgs());
				ret.setChatStyle(old.getChatStyleIfPresent());
				ret.getSiblings().addAll(comp.getSiblings());
				return ret;
			}else if(comp instanceof ChatComponentScore) {
				ChatComponentScore old = (ChatComponentScore)comp;
				IChatComponent ret = new ChatComponentScore(old.getName(), old.getObjective());
				ret.setChatStyle(old.getChatStyleIfPresent());
				ret.getSiblings().addAll(comp.getSiblings());
				return ret;
			}else if(comp instanceof ChatComponentSelector) {
				ChatComponentSelector old = (ChatComponentSelector)comp;
				IChatComponent ret = new ChatComponentSelector(old.getSelector());
				ret.setChatStyle(old.getChatStyleIfPresent());
				ret.getSiblings().addAll(comp.getSiblings());
				return ret;
			}
		}
		return comp.createCopy();
	}

	private static final char[] stars = new char[] { '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*',
			'*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*',
			'*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*',
			'*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*' };

	public String profanityFilterString(String stringIn) {
		if(stringIn == null) return null;
		String str = null;
		//long start = System.nanoTime();
		try {
			str = profanityFilterString0(stringIn);
		}catch(Throwable t) {
			logger.error("Profanity filter raised an exception on string!");
			logger.error(t);
		}
		//logger.info("Took: {}", ((System.nanoTime() - start) / 1000000.0));
		return str != null ? str : stringIn;
	}

	protected String profanityFilterString0(String stringIn) {
		if(StringUtils.isAllBlank(stringIn)) {
			return null;
		}
		int inLen = stringIn.length();
		boolean flag = false;
		int i = 0, j;
		int k = -1;
		StringBuilder b = null;
		String str;
		List<int[]> rangeList = new ArrayList(4);
		int minCoverage = 40;
		int pieceLen;
		while((j = nextDelimiter(stringIn, i)) != -1) {
			if(j - i > 2) {
				if(b != null) {
					str = LookAlikeUnicodeConv.convertString(stripColorCodes(b.toString())).toLowerCase();
					pieceLen = str.length();
					b = null;
					//System.out.println("\"" + str + "\"");
					rangeList.clear();
					if(isBanned(str, rangeList) && evaluateCoverage(pieceLen, rangeList) > (pieceLen * minCoverage / 100)) {
						flag = true;
						for(int m = 0, n = rangeList.size(); m < n; ++m) {
							stringIn = doStar(stringIn, k, i - 1, rangeList.get(m));
						}
					}
					k = -1;
				}
				str = LookAlikeUnicodeConv.convertString(stripColorCodes(stringIn.substring(i, j))).toLowerCase();
				pieceLen = str.length();
				//System.out.println("\"" + str + "\"");
				rangeList.clear();
				if(isBanned(str, rangeList) && evaluateCoverage(pieceLen, rangeList) > (pieceLen * minCoverage / 100)) {
					flag = true;
					for(int m = 0, n = rangeList.size(); m < n; ++m) {
						stringIn = doStar(stringIn, i, j, rangeList.get(m));
					}
				}
			}else if(j - i > 0) {
				if(b == null) {
					k = i;
					b = new StringBuilder(stringIn.substring(i, j));
				}else {
					b.append(stringIn.substring(i, j));
				}
			}
			i = j + 1;
		}
		j = inLen;
		if(j - i > 2) {
			if(b != null) {
				str = LookAlikeUnicodeConv.convertString(stripColorCodes(b.toString())).toLowerCase();
				pieceLen = str.length();
				b = null;
				//System.out.println("\"" + str + "\"");
				rangeList.clear();
				if(isBanned(str, rangeList) && evaluateCoverage(pieceLen, rangeList) > (pieceLen * minCoverage / 100)) {
					flag = true;
					for(int m = 0, n = rangeList.size(); m < n; ++m) {
						stringIn = doStar(stringIn, k, i - 1, rangeList.get(m));
					}
				}
				k = -1;
			}
			str = LookAlikeUnicodeConv.convertString(stripColorCodes(stringIn.substring(i, j))).toLowerCase();
			pieceLen = str.length();
			//System.out.println("\"" + str + "\"");
			rangeList.clear();
			if(isBanned(str, rangeList) && evaluateCoverage(pieceLen, rangeList) > (pieceLen * minCoverage / 100)) {
				flag = true;
				for(int m = 0, n = rangeList.size(); m < n; ++m) {
					stringIn = doStar(stringIn, i, j, rangeList.get(m));
				}
			}
		}else if(j - i > 0) {
			if(b == null) {
				k = i;
				b = new StringBuilder(stringIn.substring(i, j));
			}else {
				b.append(stringIn.substring(i, j));
			}
		}
		if(b != null) {
			str = LookAlikeUnicodeConv.convertString(stripColorCodes(b.toString())).toLowerCase();
			pieceLen = str.length();
			b = null;
			//System.out.println("\"" + str + "\"");
			rangeList.clear();
			if(isBanned(str, rangeList) && evaluateCoverage(pieceLen, rangeList) > (pieceLen * minCoverage / 100)) {
				flag = true;
				for(int m = 0, n = rangeList.size(); m < n; ++m) {
					stringIn = doStar(stringIn, k, stringIn.length(), rangeList.get(m));
				}
			}
		}
		return flag ? stringIn : null;
	}

	protected String doStar(String stringIn, int start, int end, int[] rangeReturn) {
		int strlen = stringIn.length();
		start += rangeReturn[0];
		end -= rangeReturn[1];
		int len = end - start;
		if(len <= 0 || start < 0 || end > strlen) {
			logger.warn("Profanity filter attempted out of bounds substring @ strlen: {}, start: {}, end: {}, len: {}", strlen, start, end, len);
			return stringIn;
		}
		StringBuilder fixed = new StringBuilder(stringIn.substring(0, start));
		fixed.append(stars, 0, MathHelper.clamp_int(len, 1, stars.length));
		fixed.append(stringIn, end, stringIn.length());
		return fixed.toString();
	}

	protected static final int[] zeroZero = new int[2];

	protected boolean isBanned(String word, List<int[]> rangeReturn) {
		int l = word.length();
		if(l == 0) return false;
		int i = bannedWords.length;
		int k;
		boolean flag = false;
		for(int j = Math.min(l, i); j >= 3; --j) {
			if(j == l) {
				Set<String> set = bannedWords[j - 1];
				//System.out.println(" - \"" + word + "\"");
				if(set != null && set.contains(word)) {
					rangeReturn.add(zeroZero);
					return true;
				}
			}else {
				Set<String> set = bannedWords[j - 1];
				if(set != null) {
					int m = l - j;
					for(int n = 0; n <= m; ++n) {
						if(overlaps(n, n + j, l, rangeReturn)) {
							//System.out.println("Skipping overlap: " + n + " -> " + (n + j));
							continue;
						}
						String ss = word.substring(n, n + j);
						//System.out.println(" - \"" + ss + "\"");
						if(set.contains(ss)) {
							rangeReturn.add(new int[] { n, m - n });
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}

	protected static boolean overlaps(int min, int max, int fullLen, List<int[]> rangeReturn) {
		int[] ii;
		int j, k;
		for(int i = 0, l = rangeReturn.size(); i < l; ++i) {
			ii = rangeReturn.get(i);
			j = ii[0];
			k = fullLen - ii[1];
			if(min < k && j < max) {
				return true;
			}
		}
		return false;
	}

	protected static int evaluateCoverage(int fullLen, List<int[]> rangeReturn) {
		int coverage = 0;
		int[] ii;
		for(int i = 0, l = rangeReturn.size(); i < l; ++i) {
			ii = rangeReturn.get(i);
			coverage += fullLen - ii[0] - ii[1] - 1;
		}
		return coverage;
	}

	protected static String stripColorCodes(String stringIn) {
		int i = stringIn.indexOf('\u00a7');
		if(i != -1) {
			int j = 0;
			int l = stringIn.length();
			StringBuilder ret = new StringBuilder(l);
			do {
				if(i - j > 0) {
					ret.append(stringIn, j, i);
				}
				j = i + 2;
			}while(j < l && (i = stringIn.indexOf('\u00a7', j)) != -1);
			if(l - j > 0) {
				ret.append(stringIn, j, l);
			}
			return ret.toString();
		}else {
			return stringIn;
		}
	}

	private static int nextDelimiter(String stringIn, int startIndex) {
		int len = stringIn.length();
		while(startIndex < len) {
			char c = stringIn.charAt(startIndex);
			switch(c) {
			case ' ':
			case '.':
			case ',':
			case '_':
			case '+':
			case '-':
			case '=':
			case '|':
			case '?':
			case '<':
			case '>':
			case '/':
			case '\\':
			case '\'':
			case '\"':
			case '@':
			case '#':
			case '%':
			case '^':
			case '&':
			case '*':
			case '(':
			case ')':
			case '{':
			case '}':
			case ':':
			case ';':
			case '`':
			case '~':
			case '\n':
			case '\r':
			case '\t':
			case '\u00a0':
				return startIndex;
			case '!':
				if (!(startIndex > 0 && !isDelimiter(stringIn.charAt(startIndex - 1)))
						|| !(startIndex + 1 < len && !isDelimiter(stringIn.charAt(startIndex + 1)))) {
					return startIndex;
				}
			default:
				break;
			}
			++startIndex;
		}
		return -1;
	}

	private static boolean isDelimiter(char c) {
		switch(c) {
		case ' ':
		case '.':
		case ',':
		case '_':
		case '+':
		case '-':
		case '=':
		case '|':
		case '?':
		case '!':
		case '<':
		case '>':
		case '/':
		case '\\':
		case '\'':
		case '\"':
		case '@':
		case '#':
		case '%':
		case '^':
		case '&':
		case '*':
		case '(':
		case ')':
		case '{':
		case '}':
		case ':':
		case ';':
		case '`':
		case '~':
		case '\n':
		case '\r':
		case '\t':
		case '\u00a0':
			return true;
		}
		return false;
	}

}