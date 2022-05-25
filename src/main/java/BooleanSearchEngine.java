import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    //??? wordSearch
    private Map<String, List<PageEntry>> wordSearch = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        List<PageEntry> entries;
        for (File pdfFile : pdfsDir.listFiles()) {

            try (var doc = new PdfDocument(new PdfReader(pdfFile))) {
                for (int i = 1; i <= doc.getNumberOfPages(); i++) {

                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                    var words = text.split("\\P{IsAlphabetic}+");

                    Set<String> setWords = Arrays.stream(words)
                            .map(String::toLowerCase)
                            .collect(Collectors.toSet());

                    for (String word : setWords) {
                        int count = Collections.frequency(Arrays.stream(words).collect(Collectors.toList()), word);
                        PageEntry pageEntry = new PageEntry(pdfFile.getName(), i, count);

                        if (!wordSearch.containsKey(word)) {
                            entries = new ArrayList<>();
                            entries.add(pageEntry);
                            wordSearch.put(word, entries);
                        } else {
                            wordSearch.
                                    get(word).
                                    add(pageEntry);
                            wordSearch.
                                    get(word).
                                    sort(Collections.reverseOrder());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> result;
        if (wordSearch.containsKey(word)) {
            result = wordSearch.get(word);
            Collections.sort(result);
        } else {
            result = new ArrayList<>(List.of(new PageEntry("По слову " + "`" + word + "`"
                    + " совпадений нет!", 0, 0)));
        }
        return result;
    }
}
