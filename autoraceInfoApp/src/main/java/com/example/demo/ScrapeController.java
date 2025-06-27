package com.example.demo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScrapeController {

    @GetMapping("/")
    public String form() {
        return "form";
    }

    @GetMapping("/scrape")
    public String scrape(@RequestParam(required = false) String url, Model model) {
        if (url == null || url.isEmpty()) return "form";

        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host == null || !host.endsWith("oddspark.com")) {
                model.addAttribute("error", "許可されたドメイン以外はアクセスできません");
                return "form";
            }
        } catch (Exception e) {
            model.addAttribute("error", "URLの形式が正しくありません");
            return "form";
        }

        List<RaceInfo> list = new ArrayList<>();
        double raceDistance = 410.0;

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();

            // レース距離と気象情報の取得
            Element rcData = doc.selectFirst("#RCdata2");
            if (rcData != null) {
                Element h3 = rcData.selectFirst("h3");
                if (h3 != null) {
                    String titleText = h3.text();
                    Pattern distancePattern = Pattern.compile("(\\d{3,4})m");
                    Matcher matcher = distancePattern.matcher(titleText);
                    if (matcher.find()) {
                        raceDistance = Double.parseDouble(matcher.group(1));
                        model.addAttribute("raceDistance", (int) raceDistance);
                    }
                }

                Element rcDist = rcData.selectFirst(".RCdst");
                if (rcDist != null) {
                    String distText = rcDist.text();
                    model.addAttribute("weather", extractValue(distText, "天候：", "走路状況："));
                    model.addAttribute("condition", extractValue(distText, "走路状況：", "走路温度："));
                    model.addAttribute("roadTemp", extractValue(distText, "走路温度：", "気温："));
                    model.addAttribute("airTemp", extractValue(distText, "気温：", "湿度："));
                    model.addAttribute("humidity", extractValue(distText, "湿度：", "（"));
                }
            }

            Elements rows = doc.select("table.tb71 tr");

            for (Element row : rows) {
                Elements tds = row.select("td");
                if (tds.size() >= 3) {
                    RaceInfo info = new RaceInfo();

                    info.setCarNo(tds.get(0).text().trim());

                    for (Element td : tds) {
                        if (td.hasClass("racer") && td.select("strong").size() > 0) {
                            info.setName(td.selectFirst("strong").text().trim());
                            break;
                        }
                    }

                    for (Element td : tds) {
                        if (td.hasClass("al-center") && !td.hasClass("lh_1-6") && td.select("br").size() > 0) {
                            info.setAffiliation(td.text().replace("\n", "").replace(" ", "").trim());
                            break;
                        }
                    }

                    try {
                        for (Element td : tds) {
                            if (td.hasClass("lh_1-6") && td.hasClass("al-center")) {
                                String[] values = td.html().split("<br\\s*/?>");
                                if (values.length >= 3 && values[0].contains("m")) {
                                    double handicap = Double.parseDouble(Jsoup.parse(values[0]).text().replace("m", "").trim());
                                    double tryTime = Double.parseDouble(Jsoup.parse(values[1]).text().trim());
                                    double deviation = Double.parseDouble(Jsoup.parse(values[2]).text().trim());

                                    info.setHandicap(handicap);
                                    info.setTryTime(tryTime);
                                    info.setDeviation(deviation);

                                    double assumedTime = tryTime + deviation;
                                    info.setAssumedTime(assumedTime);

                                    double totalDistance = raceDistance + handicap;
                                    double expectedFinishTime = totalDistance * assumedTime / 100.0;
                                    info.setExpectedFinishTime(expectedFinishTime);

                                    list.add(info);
                                    break;
                                }
                            }
                      
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
            
            Element bikeDataTable = doc.selectFirst("#ownerBikeData table.tb71");
            if (bikeDataTable != null) {
                Elements bikeRows = bikeDataTable.select("tr");
                for (Element row : bikeRows) {
                    Elements cols = row.select("td");
                    if (cols.size() >= 9) {
                        String carNo = cols.get(0).text().trim();
                        String good2 = cols.get(5).text().trim();
                        String good3 = cols.get(6).text().trim();
                        String wet2 = cols.get(7).text().trim();
                        String wet3 = cols.get(8).text().trim();

                        // 車番で RaceInfo を検索し、対応するものにセット
                        for (RaceInfo r : list) {
                            if (r.getCarNo().equals(carNo)) {
                                r.setGoodWinRate2(good2);
                                r.setGoodWinRate3(good3);
                                r.setWetWinRate2(wet2);
                                r.setWetWinRate3(wet3);
                                break;
                            }
                        }
                    }
                }
            }

            model.addAttribute("racers", list);
            
            List<RaceInfo> rankList = new ArrayList<>(list);
            rankList.sort(Comparator.comparingDouble(RaceInfo::getExpectedFinishTime));
            model.addAttribute("rankList", rankList);
            
        } catch (Exception e) {
            model.addAttribute("error", "取得失敗: " + e.getMessage());
        }

        return "form";
    }

    private String extractValue(String text, String from, String to) {
        int start = text.indexOf(from);
        int end = text.indexOf(to);
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start + from.length(), end).trim();
        }
        return "";
    }
}
