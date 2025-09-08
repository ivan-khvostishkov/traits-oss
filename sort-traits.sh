sort oxford-traits.txt | egrep -v 'though '\|'can '\|'while '\|'when '\|'but ' | cut -d ' ' -f1 | \
  sort | uniq -c | sort -n -r | tee oxford-traits-sorted.txt | less
