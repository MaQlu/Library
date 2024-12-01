import { Separator } from "@radix-ui/react-separator";
import BookCard from "./book-card";
import { Book } from "@/interfaces";
import { ScrollArea, ScrollBar } from "./ui/scroll-area";


export default async function BooksRow({ title, books }: {title: string, books?: Book[]}) {
  const shuffle = (arr: Book[]) => {
    let count = arr.length,
        randomNumber,
        temp;
    const random = Math.random();
    while (count) {
      randomNumber = random * count-- | 0;
      temp = arr[count];
      arr[count] = arr[randomNumber];
      arr[randomNumber] = temp;
    }
  }
  
  const bks: Book[] = await (async () => {
    if (books) return books;
    const res = await fetch(process.env.GATEWAY_URL + "waz/books/");
    const b: Book[] = await res.json();
    shuffle(b);
    return b.slice(0, 15);
  })()

  return (
    <div className="flex flex-col gap-2">
      <h2 className="text-2xl pt-2 leading-none">{title}</h2>
      <Separator orientation="horizontal" className="mb-1"/>
      <ScrollArea className="">
        <div className="flex w-max space-x-2">
          {bks.map((bookData) => 
            <div key={bookData.id} className="w-40">
              <BookCard bookData={bookData}></BookCard>
            </div>
          )}
        </div>
        <ScrollBar orientation="horizontal" />
      </ScrollArea>
    </div>
  )
}
